Swagger-How-To:
----------------

1) Swagger web-app deployment
----------------------------------
A) unzip swagger-ui.zip inside $TOMCAT/webapps
B) vi $TOMCAT/webapps/swagger-ui/dist/index.html:
	url: "http://localhost:8080/bs/rest/api-docs",

C) You can hit swagger-ui: localhost:8080/swagger-ui/dist/index.html


2) maven
---------
		<dependency>
			<groupId>com.wordnik</groupId>
			<artifactId>swagger-jaxrs_2.10</artifactId>
			<version>1.3.0</version>
		</dependency>


3) CXF/Spring App-Ctx (beans.xml)
----------------------------------
	<jaxrs:server id="restContainer" address="/">
		<!-- 
		<jaxrs:features>
			<cxf:logging/>
		</jaxrs:features>
		 -->
		<jaxrs:serviceBeans>
			<ref bean="bookResourceImpl" />
            <ref bean="swaggerResourceJSON" />
		</jaxrs:serviceBeans>
        <jaxrs:providers>
            <bean id="resourceWriter"
                  class="com.wordnik.swagger.jaxrs.listing.ResourceListingProvider" />
            <bean id="apiWriter"
                  class="com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider" />
        </jaxrs:providers>
	</jaxrs:server>
	
	
  <!-- Swagger API listing resource -->
    <bean id="swaggerResourceJSON"
          class="com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON" />

    <bean id="swaggerConfig" class="com.wordnik.swagger.jaxrs.config.BeanConfig">
        <property name="resourcePackage" value="com.bp.bs" />
        <property name="version" value="1.0.0" />
        <property name="basePath" value="http://localhost:8080/bs/rest" />
        <property name="title" value="Books Management Services" />
        <property name="description" value="Books Management Services Description" />
        <property name="contact" value="bpirvali@gmail.com" />
        <property name="license" value="Apache Licensed" />
        <property name="licenseUrl" value="apache.org" />
        <property name="scan" value="true" />
    </bean>	


3) Service/resource swagger annotations
----------------------------------------
A) Resource-Annotations
@ApiModel( value = "Person", description = "Person resource representation" )
public class Person {
	@ApiModelProperty( value = "Person's first name", required = true ) private String email;
	@ApiModelProperty( value = "Person's e-mail address", required = true ) private String firstName;
	@ApiModelProperty( value = "Person's last name", required = true ) private String lastName;
		
B) Service-annotations
@Path( "/people" ) 
@Api( value = "/people", description = "Manage people" )
public class PeopleRestService {

@ApiOperation( value = "List all people", notes = "List all people using paging", response = Person.class, responseContainer = "List")
public Collection< Person > getPeople(  @ApiParam( value = "Page to fetch", required = true ) @QueryParam( "page") @DefaultValue( "1" ) final int page ) {
	return peopleService.getPeople( page, 5 );
}


@ApiResponses( {
    @ApiResponse( code = 201, message = "Person created successfully" ),
    @ApiResponse( code = 409, message = "Person with such e-mail already exists" )
} )




