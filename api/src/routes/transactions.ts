import auth from "../auth";
import type { Handler } from "hono";
import getClient from "../getClient";

const transactions: Handler<{ Bindings: Environment }> = async c => {
	const supabase = getClient(c.env);
	const client = await auth(supabase, c.req, c.env);
	if (client === undefined) {
		return c.text("Not authorized", 401);
	}
	const { data } = await supabase.from("transactions")
		.select("created_at, sender, receiver, amount, message")
		.or(`sender.eq.${client.id},receiver.eq.${client.id}`)
		.limit(10);
	if(data === null) {
		return c.text("Vi kunne ikke finde nogen transaktioner for dig.");
	}
	const transactions = data as Transaction[];
	if (transactions.length === 0) {
		return c.text("Vi kunne ikke finde nogen transaktioner for dig.");
	}
	return c.text(`Dine seneste ${transactions.length} transaktioner:\n${transactions.map(t => `\t${new Date(t.created_at).toLocaleString("da-dk", {
		weekday: "long",
		year: "numeric",
		month: "long",
		day: "numeric",
		hour: "numeric",
		minute: "numeric",
		timeZone: "Europe/Copenhagen"
	})} - ${t.sender} -> ${t.receiver} - ${t.amount} kr. - ${t.message}`).join("\n")}`);
};

export default transactions;