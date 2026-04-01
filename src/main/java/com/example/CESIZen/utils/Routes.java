package com.example.CESIZen.utils;

public final class Routes {

    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String REGISTER_ADMIN = "/register-admin";
    public static final String ROLE_ADMIN = "/admin";

    public static final String CREATE_USER = "/create-user";
    public static final String USER_PROFIL = "/profil";
    public static final String UPDATE_USER_PROFIL = "/update-profil";
    public static final String USER_LIST = "/users-list";
    public static final String FILTER_USER_ROLE_LIST = "/filter-users-list";
    public static final String DELETE_USER = "/delete";

    public static final String REQUEST_PASSWORD = "/request-password";
    public static final String VERIFY_PIN = "/verify-pin";
    public static final String RESET_PASSWORD = "/reset-password";

    public static final String PAGE = "/page";
    public static final String CREATE_PAGE = PAGE + "/create-page";
    public static final String GET_PAGE = PAGE + "/get-page";
    public static final String GET_ALL_PAGE = PAGE + "/get-all-pages";
    public static final String UPDATE_PAGE = PAGE + "/update-page";
    public static final String DELETE_PAGE = PAGE + "/delete-page";

    public static final String CATEGORY = "/categories";
    public static final String CREATE_CATEGORY = CATEGORY + "/create-category";
    public static final String CATEGORY_LIST = CATEGORY + "/list";

    public static final String CREATE_QUIZ = "/create-quiz";
    public static final String GET_QUIZ = "/get-quiz-by-id";
    public static final String GET_ALL_QUIZ = "/quiz-list";
    public static final String UPDATE_QUIZ = "/update-quiz";
    public static final String DELETE_QUIZ = "/delete-quiz";
    public static final String ADD_QUESTION_QUIZ = "/add-question";
    public static final String GET_ALL_QUESTIONS = "/get-all-questions";
    public static final String DELETE_QUESTION_QUIZ = "/delete-question-quiz";
    public static final String SUBMIT = "/submit";
    public static final String SAVE_RESULT = "/save-result";
    public static final String GET_ALL_HISTORY_QUIZ = "/get-history-quiz";

    public static final String RESULT_MESSAGE_CONFIG = "/result-message-config";
    public static final String RESULT_MESSAGE_CREATE = RESULT_MESSAGE_CONFIG + "/create";
    public static final String RESULT_MESSAGE_UPDATE = RESULT_MESSAGE_CONFIG + "/update";
    public static final String RESULT_MESSAGE_GET_ALL = RESULT_MESSAGE_CONFIG + "/get-all";
    public static final String RESULT_MESSAGE_DELETE = RESULT_MESSAGE_CONFIG + "/delete";

    public static final String BACKEND_CHECK = "/backend-up";

}
