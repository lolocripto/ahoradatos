package trie;

public class TestTrie {

	public static void main(String[] args) {

		System.out.println(System.currentTimeMillis());
		
		Trie T = new Trie();
		Long L = new Long(4123);
		PunteroSonido p = new PunteroSonido(L);

		T.insert("caca", p);
		T.insert("cacarusa", p);
		T.insert("cacaroto", p);
		T.insert("pis", p);
		T.insert("piso", p);
		T.insert("almeja", p);
		T.insert("gota", p);
		T.insert("go",p);
		T.insert("amigdala", p);
		T.insert("p", p);
		T.insert("w", p);

		T.search("cacarusa");
		T.search("go");
		T.search("alme");
		T.search("almeja");
		T.search("w");
		T.search("p");
		T.search("blah");
		T.search("");

		System.out.println(System.currentTimeMillis());
	}

}