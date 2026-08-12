package cards.mpay.uapi.model.holder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A cardholder's delivery address, embedded in {@link HolderInfo}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryAddress {

    private String country;
    private String state;
    private String city;
    private String street;
    private String zip;

    @JsonProperty("postalCode")
    private String postalCode;

    private String district;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "DeliveryAddress{country='" + country + "', state='" + state + "', city='" + city
                + "', street='" + street + "', zip='" + zip + "', postalCode='" + postalCode
                + "', district='" + district + "'}";
    }
}
