package com.eloan.uiweb.config;



public class AlipayConfig {
    // 商户appid
    public static String APPID = "2016092500593266";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC367YQO4nLsIBtUl5iCMz1jwvWogUaaTGSvfPtg8Y2nywvS2uYtvv6POt2UHkynJVx4qOBEECpPaqtJDgfuOL5M4hGxjkar4ZPUQR6w6PG7rm+3kV4PfQrTBH19/jm1YASYdzFWZ0zLDMyXU7kuEvAx09BFeakoexOhhwjzRzV5+S1/lkFQSoFpKJZxkfLG10t+u+lhz3YANfYANLL//HUNGBwuR2sey5cpkvps/4jiC08ccKwHt8LxpIugYn6eof+7OF8AAdjzGkYqnd3a3Qc4uuCViOA1lP96xOFyGr2ex6Q+mTjJFuEqr//gH6MPtZ48X523K1RMKiBJJ6EVqX7AgMBAAECggEBALKAbIU4o7YzKZHwnXXcQMAbA0xel0FY0youfa4JdLjSp1mHg3gGMGxf7882r0o1ur17T2EV2wvms92zB12SXYerQhoejJGfxWT5prVXZ37Ndy0OILJgvre+7reg4ct3bwNoQ5KuNOnTfiTldop5a9xAFGWA0gb6uJRFiM4eZlwbOIkREpSwCI5XGONkQr+te3yt2Naj0qiarF8tsR6QwXnllaXVt/ji2fLOgzxccyDa/sPw0xwAdTZixZ6TL/YempXC+IYTHitJtEpkoEhO5L3QXL3OJ3WVdnbpgAxJMS+EbUT6thQCD+50ErB6iW6zhEKRIQflnjspuakxnJ79W/ECgYEA+5MiDz7A+fdxcdz7TyyYpp45VYQQTHuEB/n4HNXZJQ/+z9LU6XJodG4P55Yl1gvTuXVD9KXRMa3DhScA4LENa7FYd1UuklsbIIYsn7OXfmd+S+H4f6jQwfpi0KNi8Vl9To6LoKDxDzlG2nJhSuCQJyMtHQ4wlctw9uFld9smb7cCgYEAuyfs3RddNRfN8fkAPAjR9NDvqd+qNpgCNpzIuULEie2pAWKkJXxVchlghivXDjEiCEnfhti+epI2ncdu1FCB7pJyCrpeM+EUxxYx2CGoKn6kwpnfzeVQ3660yZxR7MBQgKCO530VEnsC6TpJenxSsNLob6LofLfjwKYZyB3Pc90CgYABbJTNx0oqYBc5UMnvac43az/h1wyMkkpuDSE1cJR5HD0SChyAcdBbkF/wrK3sJ0b8jshisymbAWPQ9iaQHxB3Ypqgmk/YLkHaDkhkYwzhf058uXvBpUABSRyUF8iwmxQVhMMPlrmmn2liRTF9zgWi4xUXTAREZNFv5FjBs/DkwwKBgAYRhFei403OaMoYxc4shwpGIA9FBlLg2OmGaee/53mFcD5wQVxJzMd3VS7kiwOVykG0i3/m1T5KMWUqS0itF5lxvCrm4hkSikbuTE1ywe4mzxbUQHJ/pgCqvIfw/qMLlVCmZBvTbXR88po3XpyPHWtIW2sLQDiTCdp8SEe517ktAoGBAL9awu9X6W5PoJ2Nu2vCVYJ1DhbUqduVBCxUy4S57MOTJvQLUz//43+AXViYdDxcL5i6xSfLyEK6vTtiIlal9GuV/IoVq0UKOti1lQAeOzKL+O1b5U4Id2tfc1fqXkM0A+3Aaqv+uU0b9sBibQcpCZ40sR1WtvakuMcEQRf+7BFg";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://xfxnsy.natappfree.cc/callback/asynCallback.do";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://localhost:8080/callback/synCallback.do";
    // 请求网关地址
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7lkVLqoUJhDvr9ptlUpDfYyMHz6XP3+LsEwq5YQ9eykqYpJwCnRnoa/ArofOhtNkIKDzEdVS3OftIwI2KI6ulo5zG91UqFzULHBd4Wzv/4xuexJkfSVQyeG3gCBeusMYAqGR+rEZ/90zePLPNNEEe0FxLsR9ghoRWU11CFhGLWT0EBdI0USPMMIfx0JPaB1ZT7HtmZV9d9cWZrhQHQWMe6/kq9ZD10iZm7k4D1Q5VXGonXsSyiGgmcJq6wjdOekly577/i3vXuMRyHSllVVgcpLQD/k8I9tsno1UZGD6C7EjJd30M8LxQ/kiVW/hTbfey61Ju0ZqXNI65DFANSu4OQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}
