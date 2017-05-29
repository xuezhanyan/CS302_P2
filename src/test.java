import java.util.Arrays;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] actionRanking={1,7,2};
		int totalPossibility = 0;
		for (int i = 0; i < actionRanking.length; i++) {
			totalPossibility = totalPossibility + actionRanking[i];
		}
		//create array containing output*possibility  
		int returnValueProbably =1;
		int indexOfoutput_possibility = 0;
		int [] output_possibility = new int [totalPossibility];
		for(int m=0; m< actionRanking.length;m++){
			for (int n = 0;n<actionRanking[m];n++){
				output_possibility[indexOfoutput_possibility]=returnValueProbably;
				indexOfoutput_possibility++;
			}
			returnValueProbably++;
		}
		System.out.println(Arrays.toString(output_possibility));

	}

}
