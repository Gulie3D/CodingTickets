<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- IMPORTANT : URI Jakarta pour Tomcat 10/11 --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Mes Réservations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-dark mb-4">
    <div class="container">
        <span class="navbar-brand">Mes Réservations</span>
        <a href="${pageContext.request.contextPath}/events" class="btn btn-outline-light btn-sm">Retour aux événements</a>
    </div>
</nav>

<div class="container">

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-striped mb-0 align-middle">
                <thead class="table-primary">
                <tr>
                    <th>Événement</th>
                    <th>Date</th>
                    <th>Places</th>
                    <th>Total</th>
                    <th>Statut</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%-- Boucle standard JSTL sans conflit --%>
                <c:forEach items="${reservations}" var="res">
                    <tr>
                        <td class="fw-bold">${res.evenement.titre}</td>
                        <td>${res.evenement.dateEvenement}</td>
                        <td>${res.nbPlaces}</td>
                        <td>${res.montantTotal} €</td>
                        <td>
                            <c:choose>
                                <c:when test="${res.statut == 'CONFIRMEE'}">
                                    <span class="badge bg-success">Confirmée</span>
                                </c:when>
                                <c:when test="${res.statut == 'ANNULEE'}">
                                    <span class="badge bg-danger">Annulée</span>
                                </c:when>
                                <c:otherwise><span class="badge bg-secondary">${res.statut}</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${res.statut == 'CONFIRMEE'}">
                                <form action="${pageContext.request.contextPath}/reservations/cancel" method="post" onsubmit="return confirm('Voulez-vous vraiment annuler ?');">
                                    <input type="hidden" name="reservationId" value="${res.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Annuler</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>

                <%--@elvariable id="reservations" type=""--%>
                <c:if test="${empty reservations}">
                    <tr><td colspan="6" class="text-center py-4">Aucune réservation trouvée.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>