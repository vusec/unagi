import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.UUID;


public class Test {
	private int x[] = {0};
	
	public static void xxx(float[] arr) {
		xxx(null);
	}
	
	public static void main(String[] args) {
		
		System.out.println(9/10);
		
		PriorityQueue<Float> q = new PriorityQueue<Float>(3, new Comparator<Float>() {
			@Override public int compare(Float arg0, Float arg1) {
				return arg1.compareTo(arg0);
			}
			
		});
		q.add(1f);
		q.add(3f);
		q.add(-1f);
		System.out.println(q.poll());
		System.out.println(q.poll());
		System.out.println(q);
		
		System.out.println(1f/3);
		
		float[] weights = {44.507704f, 12.332206f, 4.318134f, 14.892757f, 15.194817f, 6.728528f, 4.650002f, 6.578515f, 3.752873f, 8.178100f, 5.110305f, 2.138472f, 5.689948f, 3.706780f, 2.639057f, 8.243513f, 8.009668f, 2.405492f, 0.744299f, 1.244696f, 1.190267f, 5.605970f, 12.561454f, 2.576368f, 8.974026f, 44.251590f, 21.886756f, 13.748566f, 10.388540f, 10.420712f, 32.375873f, 8.001813f, 7.026638f, 9.606736f, 25.629233f, 3.904439f, 59.263356f, 146.139585f, 99.047589f, 9.256923f, 6.873813f, 4.783489f, 5.313691f, 8.068317f, 0.000000f, 0.816645f, 1.054270f, 0.000000f, 4.037190f, 7.013456f, 0.000000f, 3.828402f, 6.670087f, 0.000000f, 17.119187f, 8.993874f, 6.089900f, 14.750620f, 14.911876f, 6.719099f, 3.232515f, 3.881755f, 3.261682f, 3.900223f, 3.113234f, 3.106560f, 3.241674f, 3.837424f, 3.322662f, 3.425071f, 3.049393f, 2.856083f, 3.933661f, 0.324106f};
		
		for(float v : weights) {
			if(v > 30)
				System.out.println(v);
		}
		System.out.println();
		
		
		//float v = ((float)1)/3;
		//System.out.println(String.valueOf(v));
		
//		Random rand = new Random();
//		for(int i=0; i<100; i++)
//			System.out.println( rand.nextInt(2) );
		
		Float[] floatData = new Float[74];
		System.out.println(floatData[0]);
		
		
		Test t = new Test();
		System.out.println(t.foo()[0]);
		t.foo()[0] = 1;
		System.out.println(t.foo()[0]);
		
		
		System.out.println(new SimpleDateFormat("yyyy_MM_dd__H_m_s__S__").format(new Date())+UUID.randomUUID().toString());
	}
	
	public int[] foo() {
		return x;
	}
}
