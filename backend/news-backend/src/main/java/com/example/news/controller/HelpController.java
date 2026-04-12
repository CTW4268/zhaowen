package com.example.news.controller;

import com.example.news.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帮助中心控制器
 */
@RestController
@RequestMapping("/help")
@Tag(name = "帮助中心", description = "常见问题解答")
public class HelpController {

    @GetMapping("/faq")
    @Operation(summary = "获取常见问题列表")
    public ApiResponse<List<Map<String, String>>> getFAQ() {
        List<Map<String, String>> faqList = Arrays.asList(
                createFAQ("如何收藏新闻？", "在新闻详情页面点击收藏按钮，或在不喜欢该新闻时取消收藏。收藏后可以在个人收藏页面查看。"),
                createFAQ("如何查看历史记录？", "登录后，在导航菜单中点击'阅读历史'即可查看您的浏览记录。"),
                createFAQ("如何切换国内/海外新闻？", "在首页顶部导航栏可以选择国内或海外选项卡进行切换。"),
                createFAQ("如何登录/注册？", "点击导航栏的用户图标，选择登录或注册。新用户需要先注册账号才能登录。"),
                createFAQ("忘记密码怎么办？", "目前不支持在线重置密码，请联系客服处理。"),
                createFAQ("如何反馈问题？", "在导航菜单中点击'帮助反馈'，填写反馈表单提交即可。")
        );
        return ApiResponse.success(faqList);
    }

    private Map<String, String> createFAQ(String question, String answer) {
        Map<String, String> faq = new HashMap<>();
        faq.put("question", question);
        faq.put("answer", answer);
        return faq;
    }
}
