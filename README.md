# springsecuritydemo
Spring Security，自定义实现分布式session、根据url进行权限验证。

<div>
    <h1>自测注意啦</h1>
    login.html中没有引入任何js，所以post header中csrftoken的名称不符合预期。
    <p>
        解决方式如下：
        <br/>
        <b>debug方式</b>
        <br/>
        代码位置 CsrfFilter.java:124
        <br/>
        复制csrfToken.getToken()的数据, 设置actualToken
    </p>
</div>

<div>
    <p1>原理讲解</p1>
    <a href="https://blog.csdn.net/chl87783255/article/details/120288973">SpringSecurity-自定义分布式session与url授权验证</a>
</div>
