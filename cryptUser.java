import java.util.*;

public class cryptUser {

	int p;
	int q;
	int n;
	int phi;
	int e;
	int d;
	boolean coprime;

	String name;
	Queue<String> inbox = new LinkedList<>();

	cryptUser (int p, int q, String n) {
		this.p = p;
		this.q = q;
		name = n;
		keyN();
		keyE();
		keyD();
	}

	void keyN() {
		n=p*q;
		phi=(p-1)*(q-1);
	}

	void keyE() {
		Stack<Integer> cpPhi = coprime(phi); 
		e = cpPhi.peek();
	}

	void keyD() {

		int mult=0;
		double rmd=0;
		while (rmd!=1) {
			mult++;
			rmd = (e*mult)%phi;
		}
		d=mult;  
	}

	Stack<Integer> coprime(int input) {
		Stack<Integer> coprime = new Stack<Integer>();
		for (int i=input-1; i>1; i--) {
			if (gcd(input,i))
				coprime.add(i);
		}
		return coprime;
	}

	boolean gcd(int a, int b) {
		coprime=false;
		int tmp = a%b;
		if (tmp==1) 
			coprime=true;
		else if (tmp>1)
			gcd(b,tmp);
		return coprime;		
	}

	
	void displayKey() {
		System.out.println("Public key of "+name+": "+this.e+", "+this.n);
	}
	
	
	void sendMessage (String s, int e, int n) {       
		String send = encryptMessage(s, e, n);
		inbox.add(send);
	}
	

	String viewInbox() {
		String inbx = "";
		int count=0;
		if (!inbox.isEmpty()) {
			System.out.println("Public inbox: ");    
			for (String tmp: inbox) {
				count++;
				inbx += ("Message "+count+": "+tmp+"\n");
			}
		} else
			inbx += "Public inbox is empty";
		return inbx;
	}

	String readInbox() {
		String inbx = "";
		if (!inbox.isEmpty()) {
			int i=1;
			for (String tmp: inbox) {
				inbx += ("Message "+i+" : "+decryptMessage(tmp)+"\n");
				i++;
			}
		} else
			inbx += ("Inbox is empty");
		return inbx;
	}

	static String encryptMessage(String s, int e, int n) {  
		String message = s;
		String encryptedMessage = "";
		for (int i=0; i<message.length(); i++) {
			char a = message.charAt(i); 
			int trs = toInt(a); 
			int crp = cryptAlgo(trs, n, e);
			encryptedMessage += crp+" ";
		}
		//System.out.println("Encrypted message: "+encryptedMessage);
		return encryptedMessage;
	}

	String decryptMessage(String m) {
		String word[] = m.split(" ");
		String decryptedMessage = "";
		for (int j=0; j<word.length; j++) {
			int a = Integer.parseInt(word[j]);
			int crp = cryptAlgo(a, n, d);
			char trs = toChar(crp);
			decryptedMessage += trs;
		}
		return decryptedMessage;
	}

	static int cryptAlgo(int word, int key1, int key2) { 
		// Repeating squares
		Stack<Integer> stack1 = new Stack<Integer>();
		int e=key2; 
		for (int i=1; i<=e;) {
			stack1.add(i);
			i=i*2;
		}
		int RepSize = stack1.size();

		// Select square
		Stack<Integer> stack2 = new Stack<Integer>();
		ArrayList<Integer> index = new ArrayList<Integer>();

		int inc=stack1.peek();  //take highest square first
		index.add(stack1.indexOf(stack1.peek()));
		stack2.add(stack1.pop());

		while (!stack1.isEmpty()) {
			int tmp = inc;
			int next = stack1.peek();  
			tmp += next;               
			if (tmp<=e) {
				inc += next;
				stack2.add(next);
				index.add(stack1.indexOf(next));
			}
			stack1.pop();
		}


		// mod machine 1
		Stack<Integer> values = new Stack<Integer>();
		int a=word;                                          
		int n =key1;                                       

		int tmpVal = a%n;
		values.add(tmpVal);
		for (int i=1; i<RepSize; i++) {      
			tmpVal = (tmpVal*tmpVal)%n;
			values.add(tmpVal);
		}


		// mod machine 2
		Stack<Integer> stack = new Stack<Integer>();

		int i=0;
		for (int tmp : values) {
			if (index.contains(i))
				stack.add(tmp);
			i++;
		}

		while (stack.size() >= 2) {
			int num1 = stack.pop();
			int num2 = stack.pop();
			int tmp = (num1*num2)%n;
			stack.add(tmp);
		}
		return stack.pop();
	}
	
	static int toInt(char a) {
		int value=0;
		switch (a) {
		case 'a': value=1; break; case 'b': value=8; break; case 'c': value=15; break; case 'd': value=21; break;
		case 'e': value=2; break; case 'f': value=9; break; case 'g': value=16; break; case 'h': value=22; break;
		case 'i': value=3; break; case 'j': value=10; break; case 'k': value=17; break; case 'l': value=23; break;
		case 'm': value=4; break; case 'n': value=11; break; case 'o': value=18; break; case 'p': value=24; break;
		case 'q': value=5; break; case 'r': value=12; break; case 's': value=19; break; case 't': value=25; break;
		case 'u': value=6; break; case 'v': value=13; break; case 'w': value=20; break; case 'x': value=26; break;
		case 'y': value=7; break; case 'z': value=14; break; case ' ': value=-1; break;
		}
		return value; 

	}

	static char toChar(int a) {
		char value='?';
		switch (a) {
		case 1: value='a'; break; case 8: value='b'; break; case 15: value='c'; break; case 21: value='d'; break;
		case 2: value='e'; break; case 9: value='f'; break; case 16: value='g'; break; case 22: value='h'; break;
		case 3: value='i'; break; case 10: value='j'; break; case 17: value='k'; break; case 23: value='l'; break;
		case 4: value='m'; break; case 11: value='n'; break; case 18: value='o'; break; case 24: value='p'; break;
		case 5: value='q'; break; case 12: value='r'; break; case 19: value='s'; break; case 25: value='t'; break;
		case 6: value='u'; break; case 13: value='v'; break; case 20: value='w'; break; case 26: value='x'; break;
		case 7: value='y'; break; case 14: value='z'; break; case -1: value=' '; break;
		}
		return value;
	}
}
