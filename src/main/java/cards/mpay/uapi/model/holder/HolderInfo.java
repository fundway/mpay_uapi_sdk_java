package cards.mpay.uapi.model.holder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cardholder information, as returned by {@code GET /v1/holder/info} and
 * {@code POST /v1/holder/set}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolderInfo {

    private long id;
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("delivery_address")
    private DeliveryAddress deliveryAddress;

    @JsonProperty("proof_file")
    private String proofFile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getProofFile() {
        return proofFile;
    }

    public void setProofFile(String proofFile) {
        this.proofFile = proofFile;
    }

    @Override
    public String toString() {
        return "HolderInfo{id=" + id + ", email='" + email + "', firstName='" + firstName
                + "', lastName='" + lastName + "', birthDate='" + birthDate + "', countryCode='" + countryCode
                + "', phoneNumber='" + phoneNumber + "', deliveryAddress=" + deliveryAddress
                + ", proofFile='" + proofFile + "'}";
    }
}
