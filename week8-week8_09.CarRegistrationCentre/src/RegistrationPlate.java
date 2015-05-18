
public class RegistrationPlate {
    // don't change the code which is already given to you

    // ATTENTION: the object variable types are final, meaning that their value cannot be changed!
    private final String regCode;
    private final String country;

    public RegistrationPlate(String country, String regCode) {
        this.regCode = regCode;
        this.country = country;
    }

    //@Override
    public String toString() {
        return country + " " + regCode;
    }
    
    //@Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (this.getClass() != object.getClass()) {
            return false;
        }

        RegistrationPlate compared = (RegistrationPlate) object;

        //if (this.regCode != compared.regCode()) {
            //return false;
        //}

        if (this.country != compared.country || this.regCode != compared.regCode) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        if (this.regCode == null) {
            return 7;
        }

        return this.country.hashCode() + this.regCode.hashCode();
    }

}