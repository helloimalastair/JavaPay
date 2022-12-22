interface MicroUser {
	name: string,
	id: string,
}
interface Transaction {
	created_at: string;
	sender: MicroUser;
	receiver: MicroUser;
	amount: number;
	message: string | null;
}