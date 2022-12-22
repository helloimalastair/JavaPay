import type { SupabaseClient} from "@supabase/supabase-js";

const auth = async (supabase: SupabaseClient, req: Request, env: Environment) => {
	const id = req.headers.get("id") || "";
	const kode = req.headers.get("code") || "";
	const { data } = await supabase.from("customers")
		.select("*")
		.eq("id", id)
		.eq("code", kode);
	console.log(data, id, kode);
	if(data !== null) {
		let results = data as unknown[];
		if(results.length > 0) {
			return (results[0] as Customer);
		}
	}
	return;
};
export default auth;