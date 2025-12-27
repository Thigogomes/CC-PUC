public class Integral
{
    public static double n = 1000000000;
    
    // função
    public static double f(double x){ 
        //return Math.PI*(49 - (x*x));
        //return x * x;
        //return 4 * Math.PI * x * Math.sqrt(49 - (x * x));
        return (1/(Math.sqrt(36 - (x*x)))); 
        //return ((1 + Math.cos(2*Math.PI))*25);
        //return 2*Math.PI*x*2*Math.sqrt(4-Math.pow(x-6, 2));
        //return (Math.sqrt(25-(x*x))*4);
    }
    
    public static double integral(double a, double b){
        double d = delta(a, b);
        double soma = 0;
        
        for(int i = 0; i < n; i++){
            double x = a + i * d;
            soma += f(x);
        }
        
        return soma * d;
    }
    
    public static double delta(double a, double b){
        return ((b-a)/n);
    }
    
	public static void main(String[] args) {
	    double a = 0;
	    double b = 6;
	    double result = integral(a, b);
	    //System.out.println(result);
	    System.out.printf("%.4f%n", result);

	}
}