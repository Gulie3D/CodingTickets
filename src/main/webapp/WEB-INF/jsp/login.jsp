<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- IMPORTANT : On garde l'URI Jakarta pour Tomcat 10/11 --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Connexion - CodingTickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #eef2f7;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .card {
            border: none;
            border-radius: 15px;
            shadow: 0 10px 25px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card p-5">
                <div class="text-center mb-5">
                    <h2 class="text-primary fw-bold display-6">CodingTickets</h2>
                    <p class="text-muted fs-5">Bienvenue ! Connectez-vous pour continuer.</p>
                </div>

                <%-- Gestion des erreurs --%>
                <%--@elvariable id="error" type=""--%>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger shadow-sm" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                    </div>
                </c:if>

                <%-- Gestion succès déconnexion --%>
                <c:if test="${param.logout == 'true'}">
                    <div class="alert alert-success shadow-sm text-center">
                        Vous avez été déconnecté avec succès.
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-4">
                        <label for="email" class="form-label fw-bold text-secondary">Adresse Email</label>
                        <input type="email" class="form-control form-control-lg bg-light" id="email" name="email"
                               required placeholder="Indiquez votre adresse email">
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label fw-bold text-secondary">Mot de passe</label>
                        <input type="password" class="form-control form-control-lg bg-light" id="password" name="password"
                               required placeholder="Indiquez votre mot de passe">
                    </div>

                    <div class="d-grid gap-2 mt-5">
                        <button type="submit" class="btn btn-primary btn-lg py-3 fw-bold shadow-sm">
                            SE CONNECTER
                        </button>
                    </div>
                </form>

                <div class="text-center mt-4 text-muted">
                    <small><strong>Comptes Organisateurs :</strong></small><br>
                    <small>bob_organisateur@coding.fr / bob123</small><br>
                    <small>marie_organisatrice@coding.fr / marie123</small>
                    <br><br>
                    <small><strong>Comptes Clients :</strong></small><br>
                    <small>alice_cliente@coding.fr / alice123</small><br>
                    <small>charlie_client@coding.fr / charlie123</small><br>
                    <small>diana_cliente@coding.fr / diana123</small>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>