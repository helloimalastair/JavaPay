import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.print("Velkommen til JavaPay.\n冰淇淋 Technologies Ltd. 2022\n\n");
		Scanner input = new Scanner(System.in);
		APIClient api = LoginMenu(input);
		menu(input, api);
	}

	public static APIClient LoginMenu(Scanner input) {
		int choice = 0;
		APIClient api = null;
		while (choice == 0) {
			System.out.print("Du skal nu vælge en mulighed fra login menuen.\nDu vælger en af mulighederne ved at indtaste nummeret i boksen []. Derefter tryk ENTER.\n\n\t[1] Login\n\t[2] Opret konto\n\n> ");
			choice = Integer.parseInt(input.nextLine());
			switch (choice) {
				case 1: {
					System.out.print("Indtast dit telefonnummer (+45 12 34 56 78):\n> ");
					String id = input.nextLine();
					System.out.print("Indtast din JavaPay kode (fire numeristiske cifre):\n> ");
					Integer code = Integer.parseInt(input.nextLine());
					api = new APIClient(id, code);
					if(api.issueOnCreate()) {
						System.out.println("Det ser ud til at der skete en fejl under login. Prøv igen.");
						continue;
					}
					choice = -1;
					break;
				} case 2: {
					System.out.print("Indtast telefonnummeret du gerne vil registrere (+45 12 34 56 78):\n> ");
					String id = input.nextLine();
					System.out.print("Indtast en kode du vil bruge til at sikre din JavaPay konto(fire numeristiske cifre):\n> ");
					int code = Integer.parseInt(input.nextLine());
					System.out.print("Indtast dit CPR-nummer (10 cifre):\n> ");
					String cpr = input.nextLine();
					System.out.print("Indtast dit navn:\n> ");
					String name = input.nextLine();
					System.out.print("Indtast din saldo:\n> ");
					double balance = Double.parseDouble(input.nextLine());
					APIClient.createAccount(id, cpr, name, code, balance);
					api = new APIClient(id, code);
					if(api.issueOnCreate()) {
						System.out.println("Det ser ud til at der skete en fejl under login. Prøv igen.");
						continue;
					}
					choice = -1;
					break;
				}
			}
			if(choice != -1) {
				choice = 0;
			}
		}
		if(api == null) {
			System.out.println("Der skete en fejl under login. Se venligst fejlmeddelelsen ovenfor.");
			System.exit(1);
		}
		return api;
	}

	public static void menu(Scanner input, APIClient api) {
		int choice = 0;
		while (true) {
			System.out.print("Vælg en af disse muligheder ved at indtaste tallet der står i firkanten.\n\n\t[1] Se liste over kontoer og tlf nummer.\n\t[2] Send penge til tlf nummer.\n\t[3] Se din saldo.\n\t[4] Se dine seneste transaktioner\n\t[5] Luk programmet.\n\n> ");
			choice = Integer.parseInt(input.nextLine());
			switch (choice) {
				case 1:
					System.out.println(api.getAccounts());
					break;
				case 2:
					System.out.print("Indtast telefonnummeret du vil sende penge til (+45 12 34 56 78):\n> ");
					String to = input.nextLine();
					double amount = -1;
					while(amount < 0) {
						System.out.print("Indtast beløbet du vil sende:\n> ");
						amount = Double.parseDouble(input.nextLine());
						if(amount < 0) {
							System.out.println("Beløbet skal være større end 0.");
						}
					}
					System.out.print("Indtast en besked til modtageren:\n> ");
					String message = input.nextLine();
					System.out.println(api.sendMoney(to, amount, message));
					break;
				case 3:
					System.out.println(api.getUser("self", "balance") + " kr.");
					choice = 0;
					break;
				case 4:
					System.out.println(api.getTransactions());
					break;
				case 5:
					System.out.println("Tak fordi du brugte JavaPay. Vi ses snart igen!");
					input.close();
					System.exit(0);
			}
		}
	}
}