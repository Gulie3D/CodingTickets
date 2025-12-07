<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Événements - CodingTickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="#">CodingTickets</a>
        <div class="d-flex text-white align-items-center gap-3">
            <c:if test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">Se connecter</a>
            </c:if>
            <c:if test="${not empty sessionScope.user}">
                <span>Bonjour, ${sessionScope.user.nom}</span>

                <%-- CORRECTION ICI : On utilise le RÔLE (Enum) au lieu de la CLASSE --%>
                <c:if test="${sessionScope.user.role == 'CLIENT'}">
                    <a href="${pageContext.request.contextPath}/reservations/history" class="btn btn-primary btn-sm">Mes Réservations</a>
                </c:if>

                <c:if test="${sessionScope.user.role == 'ORGANISATEUR'}">
                    <a href="${pageContext.request.contextPath}/events/my" class="btn btn-warning btn-sm">Mes Événements</a>
                    <a href="${pageContext.request.contextPath}/events/create" class="btn btn-success btn-sm">+ Créer</a>
                </c:if>

                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Déconnexion</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="container">
    <h2 class="mb-4 text-center">Tous les événements</h2>

    <%--@elvariable id="errorMessage" type=""--%>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="row">
        <%--@elvariable id="events" type="java.util.List"--%>
        <c:forEach items="${events}" var="event">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column">
                        <div class="d-flex justify-content-between align-items-start">
                            <h5 class="card-title fw-bold">${event.titre}</h5>
                            <span class="badge bg-success">${event.prixBase} €</span>
                        </div>
                        <p class="text-muted small mb-2"><i class="bi bi-geo-alt"></i> ${event.lieu} | ${event.dateEvenement}</p>
                        <p class="card-text text-secondary flex-grow-1">${event.description}</p>
                        <hr>

                        <div class="d-flex justify-content-between align-items-center">
                            <c:choose>
                                <c:when test="${event.nbPlacesRestantes == 0}">
                                    <span class="badge bg-danger">COMPLET</span>
                                </c:when>
                                <c:otherwise>
                                    <small>Restantes : <b>${event.nbPlacesRestantes}</b> / ${event.nbPlacesTotales}</small>
                                </c:otherwise>
                            </c:choose>

                                <%-- CORRECTION ICI AUSSI : Vérification du RÔLE CLIENT --%>
                            <c:if test="${event.nbPlacesRestantes > 0 && sessionScope.user.role == 'CLIENT'}">
                                <form action="${pageContext.request.contextPath}/reservations/create" method="post" class="d-flex gap-2">
                                    <input type="hidden" name="eventId" value="${event.id}">
                                    <input type="number" name="nbPlaces" value="1" min="1" max="${event.nbPlacesRestantes}" class="form-control form-control-sm" style="width: 60px;">
                                    <button type="submit" class="btn btn-primary btn-sm">Réserver</button>
                                </form>
                            </c:if>

                            <c:if test="${empty sessionScope.user}">
                                <small class="text-muted fst-italic">Connectez-vous pour réserver</small>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>