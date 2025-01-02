package com.hust.documentweb.constant;

public class CommonConstrant {
    public static final String DEFAULT_AVATAR = "uploads/avatars/default.jpg";
    public static final String KEY = "?key=";
    public static final String INTERNAL = "INTERNAL";
    public static final String COMMUNITY = "COMMUNITY";
    public static final String POST_TYPE = "postType";

    public static final String NEW_OPERATOR = "＆（）｜＝＞＜～［］！，≠≁";
    public static final String FORMAT =
            "tạo 20 câu hỏi trắc nhiệm có 4 đáp án, ký tự * nằm trước đáp án đúng , hãy trả lời theo đúng format :"
                    + " ## đề tài \n\n**Câu 1:**  câu hỏi 1\n* a) đáp án a\nb) đáp án b\nc) đáp án c\nd) đáp án d\n\n**Câu 2:**  câu hỏi 2:\na) đáp án a\n* b) đáp án b\nc) đáp án c\nd) đáp án d ,"
                    + " sao cho các đáp án đúng đuợc phân boor đều từ a đến d,"
                    + " câu hỏi chỉ nằm trên 1 dòng và các đáp án chỉ nằm trên 1 dòng,"
                    + " nếu câu hỏi và đáp án có ký tự đặc biệt thì hãy viết dưới dạng format của katex của reactjs,"
                    + " theo chủ đề : ";

    // ＆（）｜＝＞＜～［］！
    public static final String AND = "＆";
    public static final String OR = "｜";
    public static final String EQUAL = "＝";
    public static final String GREATER_THAN = "＞";
    public static final String LESS_THAN = "＜";
    public static final String GREATER_OR_EQUAL = "＞＝";
    public static final String LESS_OR_EQUAL = "＜＝";
    public static final String LIKE = "～";
    public static final String LEFT_BRACKET = "［";
    public static final String RIGHT_BRACKET = "］";
    public static final String RIGHT_PARENTHESES = "）";
    public static final String LEFT_PARENTHESES = "（";
    public static final String COMMA = "，";
    public static final String PERCENT = "%";
    public static final String EQUAL_LEFT_BRACKET = "＝［";
    public static final String NOT_EQUAL = "≠";
    public static final String NOT_LIKE = "≁";
}
