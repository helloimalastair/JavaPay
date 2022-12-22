import { createClient } from "@supabase/supabase-js";

export default function(e: Environment) {
	return createClient(e.SUPABASE_URL, e.SUPABASE_TOKEN);
};