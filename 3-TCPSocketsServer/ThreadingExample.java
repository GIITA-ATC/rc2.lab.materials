public class ThreadingExample {
    public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println("Usage: java ThreadingExample <word1> <word2> ... <wordN>");
			System.exit(1);
		}

		String[] words = args;
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
				// All words will be printed at the same time despite the sleep
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Error with thread: " + e.getMessage());
			}
		}
	}
}
