import java.net.URI;
import java.net.URLEncoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpResponse.BodyHandlers;

public class APIClient {
	private static HttpClient client = HttpClient.newHttpClient();
	private boolean issueOnCreate;
	private String id;
	private int code;
	private static String URLEncode(String s) {
		try {
			return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new Error("Error: Could not encode string");
		}
	}
	public static void createAccount(String id, String cpr, String name, int code, double balance) {
		String url = "https://javapay.alastair-technologies.workers.dev/create?id=" + URLEncode(id) + "&cpr=" + URLEncode(cpr) + "&name=" + URLEncode(name) + "&code=" + code + "&balance=" + balance;
		// System.out.println(url);
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.build();
		try {
			client.send(request, BodyHandlers.ofString());
		} catch(IOException | InterruptedException e) {
			throw new Error("Error: Could not create account");
		}
	}
	APIClient(String id, int code) {
		this.id = id;
		this.code = code;
		String name = getUser("self", "name");
		if(name == null || name == "Not Authorized" || name == "Not Found") {
			issueOnCreate = true;
		}
		System.out.println("Velkommen, " + name + "!");
	}
	public String getUser(String target, String arg) {
		String id;
		if(target == "self") {
			id = this.id;
		} else {
			id = target;
		}
		try {
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://javapay.alastair-technologies.workers.dev/user?id=" + URLEncode(id) + "&arg=" + arg))
				.header("id", id)
				.header("code", Integer.toString(code))
				.build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			return response.body();
		} catch(IOException | InterruptedException e) {
			return null;
		}
	}
	public boolean issueOnCreate() {
		return issueOnCreate;
	}
	public String getAccounts() {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://javapay.alastair-technologies.workers.dev/accounts"))
			.header("id", id)
			.header("code", Integer.toString(code))
			.build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			return response.body();
		} catch(IOException | InterruptedException e) {
			return null;
		}
	}
	public String sendMoney(String to, double amount, String message) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://javapay.alastair-technologies.workers.dev/send" + "?to=" + URLEncode(to) + "&amount=" + amount + "&message=" + URLEncode(message)))
			.header("id", id)
			.header("code", Integer.toString(code))
			.build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			return response.body();
		} catch(IOException | InterruptedException e) {
			return null;
		}
	}
	public String getTransactions() {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://javapay.alastair-technologies.workers.dev/transactions"))
			.header("id", id)
			.header("code", Integer.toString(code))
			.build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			return response.body();
		} catch(IOException | InterruptedException e) {
			return null;
		}
	}
}
