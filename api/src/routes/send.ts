import auth from "../auth";
import type { Handler } from "hono";
import getClient from "../getClient";
import { nanoid } from "nanoid";

const send: Handler<{ Bindings: Environment }> = async c => {
	const supabase = getClient(c.env);
	const customer = await auth(supabase, c.req, c.env);
	if (customer === undefined) {
		return c.text("Not authorized", 401);
	}
	const params = new URL(c.req.url).searchParams;
	const { data: target } = await supabase.from("customers")
		.select("*")
		.eq("id", params.get("to"));
	if(target === null) {
		return c.text("Telefonnummeret du gav ser ikke ud til at være registreret hos JavaPay. Prøv igen.", 400);
	}
	const amount = Number(params.get("amount"));
	if(customer.balance < amount) {
		return c.text("Insufficient Funds", 400);
	}
	console.log(await supabase.rpc("transact", {
		trans_id: nanoid(),
		created_at: new Date().toISOString(),
		sender: customer.id,
		recipient: params.get("to") as string,
		amount,
		message: params.get("message"),
	}));
	return c.text(`Du har nu sendt ${amount} kr. til ${target[0].name}.`);
};

export default send;