import { Hono } from "hono";
import { send, user, create, accounts, transactions } from "./routes";

const app = new Hono<{ Bindings: Environment }>();

app.get("/send", send);
app.get("/user", user);
app.get("/create", create);
app.get("/accounts", accounts);
app.get("/transactions", transactions);

export default {
	fetch: app.fetch
};