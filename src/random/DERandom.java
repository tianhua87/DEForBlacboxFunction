package random;

import java.util.Random;

public class DERandom extends Random{
        public double nextDouble(double low, double high){
            return super.nextDouble()*(high - low) + low;
        }
}
