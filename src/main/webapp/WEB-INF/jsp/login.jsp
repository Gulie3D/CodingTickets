<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - CodingTickets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="login-page">

<div class="login-container">
    <div class="login-card">
        <h1 class="login-logo">CodingTickets</h1>
        <p class="login-subtitle">Connectez-vous à votre compte</p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" 
                       required placeholder="exemple@email.com">
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Mot de passe</label>
                <input type="password" class="form-control" id="password" name="password" 
                       required placeholder="••••••••">
            </div>

            <button type="submit" class="btn btn-primary btn-full">Se connecter</button>
        </form>

        <div class="divider"></div>
        
        <p class="demo-title">Comptes de démonstration</p>
        
        <div class="demo-accounts">
            <div class="demo-account">
                <span class="demo-label">Organisateurs</span>
                <span class="demo-email">bob_organisateur@coding.fr</span>
                <span class="demo-pass">bob123</span>
                <span class="demo-email">marie_organisatrice@coding.fr</span>
                <span class="demo-pass">marie123</span>
            </div>
            <div class="demo-account">
                <span class="demo-label">Clients</span>
                <span class="demo-email">alice_cliente@coding.fr</span>
                <span class="demo-pass">alice123</span>
                <span class="demo-email">charlie_client@coding.fr</span>
                <span class="demo-pass">charlie123</span>
                <span class="demo-email">diana_cliente@coding.fr</span>
                <span class="demo-pass">diana123</span>
            </div>
        </div>
    </div>
</div>

</body>
</html>
