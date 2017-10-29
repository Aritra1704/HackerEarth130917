package in.arpaul.inshorts.webservices;


import in.arpaul.inshorts.BuildConfig;

/**
 * Created by Aritra on 01-08-2016.
 */
public class WSConstants {

    public final static int TYPE_DEFAULT                = 0;

    public final static int STATUS_SUCCESS              = 200;
    public final static int STATUS_CREATED              = 201;
    public final static int STATUS_ACCEPTED             = 202;
    public final static int STATUS_NO_CONTENT           = 204;
    public final static int STATUS_FAILED               = 500;
    public final static int STATUS_INVALID_SERVICE      = 501;
    public final static int STATUS_INVALID_API_KEY      = 401;

    public final static int READ_TIMEOUT                = 60000;
    public final static int WRITE_TIMEOUT               = 60000;
    public final static int CONNECT_TIMEOUT             = 10000;
    public final static int IMAGE_WRITE_TIMEOUT         = 120000;

    public static class ServerKeys {
        public final static String contentType = "Content-Type";
        public final static String apiKey = "x-zippr-api-key";
        public final static String sessionToken = "x-zippr-sessiontoken";
        public static final String CONTENT_TYPE = "application/json";
    }

//    public static final String API_KEY = BuildConfig.API_KEY;

    public final static String GET      = "GET";
    public final static String POST     = "POST";

    public final static String RESPONSE_STATUS          = "ok";
    public final static String RESPONSE_BODY            = "response";
    public final static String RESPONSE_ERROR           = "error";
    public final static String RESPONSE_STATUS_TRUE     = "true";
    public final static String RESPONSE_REASON          = "reason";

    public final static String DOWNLOADPATH     = "DOWNLOADPATH";
    public final static String FILENAME         = "FILENAME";

    public final static String STAT_SUCCESS  = "SUCCESS";
    public final static String STAT_FAILURE  = "FAILURE";

    public static class APIInputs {
        public final static String param    = "param";
        public final static String body     = "body";
    }

    public static class SendOTP {
        public final static String REQUEST_MOBILE_NUMBER    = "mobile_num";
        public final static String REQUEST_OTP_SECRET       = "otp_secret";

        public final static String RESPONSE_MESSAGE = "message";
    }

    public static class LoginOTP {
        public final static String REQUEST_MOBILE_NUMBER    = "mobile_num";
        public final static String REQUEST_OTP              = "otp";
        public final static String REQUEST_OTP_SECRET       = "otp_secret";

        public final static String RESPONSE_USER            = "user";
    }

    public static class UserDetail {
        public final static String REQUEST_MOBILE_NUMBER    = "mobile_num";

        public final static String RESPONSE_USER            = "user";
    }

    public static class AddEditAgent {
        public final static String REQUEST_ID               = "_id";
        public final static String REQUEST_MOBILE_NUMBER    = "mobile_num";
        public final static String REQUEST_NAME             = "name";
        public final static String REQUEST_RECOVERY_MAIL    = "email";
        public final static String REQUEST_ALT_NUM          = "alt_num";
        public final static String REQUEST_CLIENT_ID        = "client_id";

        public final static String RESPONSE_AGENT           = "_id";
    }

    public static class AssignAgent {
        public final static String REQUEST_ORDER_NUM        = "order_num";
        public final static String REQUEST_DEL_AGENT_ID     = "del_agent_id";
        public final static String REQUEST_DEL_AGENT_PHONE  = "del_agent_phone";
        public final static String REQUEST_CLIENT_ID        = "client_id";

        public final static String RESPONSE_AGENT           = "_id";
    }

    public static class VerifyCustAddress {
        public final static String REQUEST_MOBILE_NUMBER    = "mobile_num";
        public final static String REQUEST_OTP              = "otp";
        public final static String REQUEST_OTP_SECRET       = "otp_secret";
        public final static String REQUEST_DDN              = "ddn";

        public final static String RESPONSE_ADDRESS         = "address";
    }

    public static class VerifyPhNum {
        public final static String REQUEST_PH_NUM           = "mobile_num";

        public final static String RESPONSE_DDN            = "ddn";
    }

    public static class SubmitOrder {
        public final static String REQUEST_CUST_NAME        = "cust_name";
        public final static String REQUEST_CUST_NUM         = "cust_num";
        public final static String REQUEST_SOURCE_ZIPPR     = "source_zippr";
        public final static String REQUEST_CLIENT_ID        = "client_id";
        public final static String REQUEST_MGR_ID           = "mgr_id";
        public final static String REQUEST_STORE_ID         = "store_id";
        public final static String REQUEST_DESTN_ADDR       = "destn_addr";
        public final static String REQUEST_SOURCE_ADDR      = "source_addr";
        public final static String REQUEST_SOURCE_LOC       = "source_loc";
        public final static String REQUEST_DESTN_ZIPPR      = "destn_zippr";
        public final static String REQUEST_ORDER_NUM        = "order_num";

        public final static String RESPONSE_ORDER           = "order_id";
        public final static String RESPONSE_CUST_NUM        = "cust_num";
        public final static String RESPONSE_DESTN_ZIPPR     = "destn_zippr";
        public final static String RESPONSE_DESTN_ADDR      = "destn_addr";
        public final static String RESPONSE_SOURCE_ZIPPR    = "source_zippr";
        public final static String RESPONSE_SOURCE_ADDR     = "source_addr";
        public final static String RESPONSE_SOURCE_LOC      = "source_loc";
        public final static String RESPONSE_CLIENT_ID       = "client_id";
        public final static String RESPONSE_STATUS          = "status";
        public final static String RESPONSE_MGR_ID          = "mgr_id";
        public final static String RESPONSE_STORE_ID        = "store_id";
        public final static String RESPONSE_ORDER_NUM       = "order_num";
        public final static String RESPONSE_CREATED_AT      = "created_at";
        public final static String RESPONSE_MODIFIED_AT     = "modified_at";
        public final static String RESPONSE_ID              = "_id";
    }

    public static class SERVICEKEYS {
        public final static String USER_ID                  = "_id";
        public final static String USER_TYPE                = "user_type";
        public final static String USER_FIRST_NAME          = "first_name";
        public final static String USER_LAST_NAME           = "last_name";
        public final static String USERNAME                 = "username";
        public final static String USER_PRIMARY_PHONE       = "primary_phone";
        public final static String USER_AUTH_DATA           = "auth_data";
        public final static String USER_COUNTRYCODE         = "countrycode";
        public final static String USER_REF_CODE            = "ref_code";
        public final static String USER_CREATED_AT          = "createdAt";
        public final static String USER_UPDATED_AT          = "updatedAt";
        public final static String USER_SOURCE_ZIPPR        = "source_zippr";
        public final static String USER_SESSIONS            = "user_sessions";
        public final static String USER_SESSIONTOKEN        = "x-zippr-sessiontoken";
    }
}
