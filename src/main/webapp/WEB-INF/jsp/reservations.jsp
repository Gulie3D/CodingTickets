<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Réservations - CodingTickets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reservations.css">
</head>
<body>

<nav class="navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/events">CodingTickets</a>
        <div class="navbar-actions">
            <span class="user-info">${sessionScope.user.nom} (${sessionScope.user.role})</span>
            <a href="${pageContext.request.contextPath}/events" class="btn btn-outline-primary btn-sm">Événements</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Déconnexion</a>
        </div>
    </div>
</nav>

<div class="page-container">
    <div class="page-header">
        <h1 class="page-title">Mes réservations</h1>
        <p class="page-subtitle">Historique de vos réservations</p>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <div class="card">
        <table class="table">
            <thead>
                <tr>
                    <th>Événement</th>
                    <th>Date</th>
                    <th>Places</th>
                    <th>Montant</th>
                    <th>Statut</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${reservations}" var="res">
                    <tr>
                        <td>
                            <strong>${res.evenement.titre}</strong>
                            <div class="text-muted text-small">${res.evenement.lieu}</div>
                        </td>
                        <td>${res.evenement.dateFormatee}</td>
                        <td><span class="badge">${res.nbPlaces} place(s)</span></td>
                        <td><strong>${res.montantTotal}€</strong></td>
                        <td>
                            <c:choose>
                                <c:when test="${res.statut == 'CONFIRMEE'}">
                                    <span class="badge bg-success">Confirmée</span>
                                </c:when>
                                <c:when test="${res.statut == 'ANNULEE'}">
                                    <span class="badge bg-danger">Annulée</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge">${res.statut}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${res.statut == 'CONFIRMEE'}">
                                <form action="${pageContext.request.contextPath}/reservations/cancel" method="post"
                                      onsubmit="return confirm('Annuler cette réservation ?');">
                                    <input type="hidden" name="reservationId" value="${res.id}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">Annuler</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty reservations}">
                    <tr>
                        <td colspan="6" class="empty-state">
                            <h5>Aucune réservation</h5>
                            <p>Vous n'avez pas encore de réservation.</p>
                            <a href="${pageContext.request.contextPath}/events" class="btn btn-primary">Voir les événements</a>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
