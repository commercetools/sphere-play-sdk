package de.commercetools.sphere.client.shop;

import de.commercetools.sphere.client.CommandRequest;
import de.commercetools.sphere.client.FetchRequest;
import de.commercetools.sphere.client.QueryRequest;
import de.commercetools.sphere.client.shop.model.Address;
import de.commercetools.sphere.client.shop.model.Customer;
import de.commercetools.sphere.client.shop.model.CustomerToken;
import de.commercetools.sphere.client.shop.model.CustomerUpdate;

import com.google.common.base.Optional;

/** Sphere HTTP API for working with customers in a given project. */
public interface CustomerService extends BasicCustomerService {
    /** Creates a request that finds a customer by given id. */
    FetchRequest<Customer> byId(String id);

    /** Finds a customer with given credentials. */
    FetchRequest<AuthenticatedCustomerResult> byCredentials(
            String email, String password);

    /** Creates a request that queries all customers. */
    QueryRequest<Customer> all();

    /** Creates a new customer. */
    CommandRequest<Customer> signup(
            String email, String password, String firstName, String lastName, String middleName, String title);

    /** Creates a new customer and associates the anonymous cart to it. */
    CommandRequest<AuthenticatedCustomerResult> signupWithCart(
            String email, String password, String firstName, String lastName, String middleName, String title, String cartId, int cartVersion);

    /** Sets a new customer password. */
    CommandRequest<Optional<Customer>> changePassword(
            String customerId, int customerVersion, String currentPassword, String newPassword);

    /** The address in shippingAddresses list referenced by addressIndex is replaced with the given address. */
    CommandRequest<Customer> changeAddress(
            String customerId, int customerVersion, int addressIndex, Address address);

    /** Removes the address in shippingAddresses list referenced by addressIndex. */
    CommandRequest<Customer> removeAddress(
            String customerId, int customerVersion, int addressIndex);

    /** The Customer.defaultShippingAddress is set to addressIndex. */
    CommandRequest<Customer> setDefaultShippingAddress(
            String customerId, int customerVersion, int addressIndex);

    /** Updates a customer with the CustomerUpdate object. */
    CommandRequest<Customer> updateCustomer(
            String customerId, int customerVersion, CustomerUpdate customerUpdate);

    /** Sets a new password.
     *
     * Requires a token that was previously generated using the {@link #createPasswordResetToken(String)} method. */
    CommandRequest<Customer> resetPassword(
            String customerId, int customerVersion, String tokenValue, String newPassword);

    /** Creates a token used to verify customer's email (set the Customer.isEmailVerified to true).
     * The ttlMinutes sets the time-to-live of the token in minutes. The token becomes invalid after the ttl expires.
     * Maximum ttlMinutes value can be 1 month. The created token is then used with the confirmEmail method.
     *
     * Customer's email could be verified as follows:
     *   1. Customer click on a verify email button.
     *   2. A token is created with the createEmailVerificationToken for the current customer.
     *   3. A link containing the token is sent to the customer by email.
     *   4. The link points to a page where the customer has to log in (if not already logged in) which calls the
     *      confirmEmail command with the customer and the token value extracted from the link. */
    CommandRequest<CustomerToken> createEmailVerificationToken(
            String customerId, int customerVersion, int ttlMinutes);

    /** Sets {@link Customer#isEmailVerified} to true.
     *
     * Requires a token that was previously generated using the {@link #createEmailVerificationToken} method. */
    CommandRequest<Customer> confirmEmail(
            String customerId, int customerVersion, String tokenValue);

}