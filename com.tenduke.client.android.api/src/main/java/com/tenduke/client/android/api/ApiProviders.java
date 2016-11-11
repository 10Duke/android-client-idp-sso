package com.tenduke.client.android.api;

import com.tenduke.client.ApiConfiguration;
import com.tenduke.client.ApiCredentials;
import com.tenduke.client.ApiProvider;
import com.tenduke.client.ApiProviderRegistry;


/** Registry of API-providers.
 * 
 */
public class ApiProviders {
    
    private final ApiProviderRegistry _registry;
    private ApiCredentials _credentials = null;


    /** Constructs new instance with given {@link ApiProviderRegistry}.
     * 
     *  @param registry 
     */
    public ApiProviders(/*@NonNull*/ final ApiProviderRegistry registry) {
        //
        _registry = registry;
    }


    /** Constructs new instance from given {@link ApiConfiguration}.
     * 
     *  @param configuration 
     */
    public ApiProviders(/*@NonNull*/ final ApiConfiguration configuration) {
        //
        _registry = new ApiProviderRegistry(configuration);
    }
    

    /** Creates default provider
     * 
     *  @param <T>
     *  @param apiClass
     *  @return 
     */
    public <T> ApiProvider<T> createDefaultProvider (final Class<T> apiClass) {
        return _registry.createDefaultApiProvider(apiClass);
    }


    /** Returns a functional API for given API class.
     * 
     *  @param <T>
     *  @param apiClass
     *  @return 
     */
    public <T> T provide (final Class<T> apiClass) {
        //
        return _registry.getProvider(apiClass).provide (_credentials);
    }


    /** Sets current credentials.
     * 
     * @param credentials 
     */
    public void setCredentials (final ApiCredentials credentials) {
        _credentials = credentials;
    }


    /** Adds an ApiProvider to the registry.
     * 
     * @param <T>
     * @param apiClass
     * @param provider
     * @return 
     */
    public <T> ApiProviders withProvider (final Class<T> apiClass, final ApiProvider<T> provider) {
        _registry.register(apiClass, provider);
        return this;
    }
    

    /** Adds default ApiProvider for given API class.
     * 
     * @param <T>
     * @param apiClass
     * @return 
     */
    public <T> ApiProviders withProviderFor (final Class<T> apiClass) {
        return withProvider(apiClass, createDefaultProvider (apiClass));
    }
    

}
