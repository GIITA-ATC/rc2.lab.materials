public class ThreadingExample {
    public static void main(String args[]) {
		String[] words = args[0].split(" ");
		int numThreads = words.length;

		for (int i = 0; i < numThreads; i++) {
			new WordPrinter(words[i]).start();
		}
    }

	public static class WordPrinter extends Thread {
		private final String word;

		public WordPrinter(String word) {
			this.word = word;
		}

		@Override
		public void run() {
			System.out.println("Thread " + this.getId() + ": " + word);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Error with thread: " + e.getMessage());
			}
		}
	}
}
