import auth from "../auth";
import type { Handler } from "hono";
import getClient from "../getClient";

const user: Handler<{ Bindings: Environment }> = async c => {
	const supabase = getClient(c.env);
	const customer = await auth(supabase, c.req, c.env);
	if (customer === undefined) {
		return c.text("Not authorized", 401);
	}
	const { data: target } = await supabase.from("customers")
		.select("id, name");
	const users = target as Customer[];
	return c.text(`Brugere på Javapay:\n${users.map(u => `\t${u.name} (${u.id})`).join("\n")}`);
};

export default user;