import type { Handler } from "hono";
import getClient from "../getClient";

const create: Handler<{ Bindings: Environment }> = async c => {
	const supabase = getClient(c.env);
	const params = new URL(c.req.url).searchParams;
	console.log(c.req.url);
	console.log(await supabase.from("customers")
		.insert([{
			id: params.get("id"),
			cpr: params.get("cpr"),
			name: params.get("name"),
			code: params.get("code"),
			balance: params.get("balance"),
		}]));
	return c.text("OK");
};

export default create;