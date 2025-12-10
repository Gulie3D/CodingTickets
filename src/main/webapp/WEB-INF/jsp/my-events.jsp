<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Événements - CodingTickets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/my-events.css">
</head>
<body>

<nav class="navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/events">CodingTickets</a>
        <div class="navbar-actions">
            <span class="user-info">${sessionScope.user.nom} (${sessionScope.user.role})</span>
            <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary btn-sm">+ Créer</a>
            <a href="${pageContext.request.contextPath}/events" class="btn btn-outline-primary btn-sm">Événements</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Déconnexion</a>
        </div>
    </div>
</nav>

<div class="page-container">
    <div class="page-header">
        <h1 class="page-title">Mes événements</h1>
        <p class="page-subtitle">Gérez les événements que vous avez créés</p>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">Événement modifié avec succès</div>
    </c:if>
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="card">
        <table class="table">
            <thead>
                <tr>
                    <th>Événement</th>
                    <th>Date</th>
                    <th>Lieu</th>
                    <th>Places</th>
                    <th>Recette</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${myEvents}" var="ev">
                    <c:set var="reservees" value="${ev.nbPlacesTotales - ev.nbPlacesRestantes}" />
                    <tr>
                        <td>
                            <strong>${ev.titre}</strong>
                            <div class="text-muted text-small">${ev.description}</div>
                        </td>
                        <td>${ev.dateFormatee}</td>
                        <td>${ev.lieu}</td>
                        <td>
                            <div class="places-info">
                                <div class="progress-bar-container">
                                    <div class="progress-bar-fill ${ev.nbPlacesRestantes == 0 ? 'full' : ''}"
                                         style="width: ${(reservees / ev.nbPlacesTotales) * 100}%"></div>
                                </div>
                                <span>${reservees}/${ev.nbPlacesTotales}</span>
                            </div>
                        </td>
                        <td><strong class="text-success">${reservees * ev.prixBase}€</strong></td>
                        <td>
                            <div class="actions">
                                <a href="${pageContext.request.contextPath}/events/edit?id=${ev.id}" class="btn btn-outline-primary btn-sm">Modifier</a>
                                   
                                <c:choose>
                                    <c:when test="${reservees == 0}">
                                        <form action="${pageContext.request.contextPath}/events/delete" method="post"
                                              onsubmit="return confirm('Supprimer cet événement ?');">
                                            <input type="hidden" name="eventId" value="${ev.id}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm">Supprimer</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-secondary btn-sm" disabled 
                                                title="Impossible : ${reservees} place(s) réservée(s)">Supprimer</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty myEvents}">
                    <tr>
                        <td colspan="6" class="empty-state">
                            <h5>Aucun événement créé</h5>
                            <p>Créez votre premier événement.</p>
                            <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary">+ Créer un événement</a>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
