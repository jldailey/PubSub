import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Util {
	public static final Iterator<String> emptyIterator = new Iterator<String>() {
		public boolean hasNext() { return false; }
		public String next() { return null; }
		public void remove() { }
	};

	public static class Trie {
		private class TrieNode {
			HashSet<String> contents = new HashSet<String>();
			HashMap<String, TrieNode> children = new HashMap<String, TrieNode>();
			public void add(String item) {
				add(item, item);
			}
			private void add(String item, String rest) {
				synchronized(this) {
					contents.add(item);
					if( item.length() == 0 || rest.length() == 0 ) return;
					String first = rest.substring(0,1);
					if( ! children.containsKey(first) ) {
						children.put(first, new TrieNode());
					}
					children.get(first).add(item, rest.substring(1));
				}
			}
			public Iterator<String> find(String prefix) {
				synchronized(this) {
					switch( prefix.length() ) {
					case 0:
						return contents.iterator();
					case 1:
						if( children.containsKey(prefix) )
							return children.get(prefix).find("");
						return emptyIterator;
					default:
						String first = prefix.substring(0,1);
						if( children.containsKey(first) )
							return children.get(first).find(prefix.substring(1));
						return emptyIterator;
					}
				}
			}
		}

		private TrieNode root = new TrieNode();
		public void add(String item) {
			root.add(item);
		}
		public Iterator<String> find(String prefix) {
			return root.find(prefix);
		}
	}

}
