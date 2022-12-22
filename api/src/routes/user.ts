import auth from "../auth";
import type { Handler } from "hono";
import getClient from "../getClient";

const user: Handler<{ Bindings: Environment }> = async c => {
	const supabase = getClient(c.env);
	const customer = await auth(supabase, c.req, c.env);
	if (customer === undefined) {
		return c.text("Not authorized", 401);
	}
	const params = new URL(c.req.url).searchParams;
	const { data: target } = await supabase.from("customers")
		.select(params.get("arg") as string)
		.eq("id", params.get("id"));
	if(target === null) {
		return c.text("Not Found", 400);
	}
	return c.text(Object.values(((target as unknown) as { [key: string]: string }[])[0]).join(","));
};

export default user;