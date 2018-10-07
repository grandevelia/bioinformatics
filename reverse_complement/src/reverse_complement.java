public class reverse_complement {
	public static final String bases = "TACG";
	public static final String complements = "ATGC";
	public static void main(String[] args) {
		boolean programRun = true;
		while (programRun){
			try{
				String inputString = args[0].toUpperCase();
				String reverseComplement = "";
				for (int i = inputString.length()-1; i >= 0; i --){
					String nextBase = Character.toString(inputString.charAt(i));
					if (bases.indexOf(nextBase) != -1){
						reverseComplement += getComplement(nextBase);
					} else {
						System.out.println("Please only enter strings" +
								" containing the letters A, T, C, or G");
						programRun = false;
						break;
					}
				}
				System.out.println(reverseComplement);
				programRun = false;
			} catch (ArrayIndexOutOfBoundsException e){
				System.out.println("This program takes one argument, a string" +
						" of nucleotides.");
				programRun = false;
			}
		}
	}
	public static String getComplement (String base){
		return Character.toString(complements.charAt(bases.indexOf(base)));
	}
}
