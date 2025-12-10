<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${editMode ? 'Modifier' : 'Créer'} un événement - CodingTickets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/event-form.css">
</head>
<body>

<nav class="navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/events">CodingTickets</a>
        <div class="navbar-actions">
            <span class="user-info">${sessionScope.user.nom} (${sessionScope.user.role})</span>
            <a href="${pageContext.request.contextPath}/events/my" class="btn btn-outline-primary btn-sm">Mes événements</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Déconnexion</a>
        </div>
    </div>
</nav>

<div class="page-container">
    <div class="form-container">
        <div class="card">
            <div class="card-body">
                <h1 class="form-title">${editMode ? 'Modifier' : 'Créer'} un événement</h1>
                <p class="form-subtitle">Remplissez les informations</p>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger">${sessionScope.error}</div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <c:if test="${editMode}">
                    <c:set var="reservees" value="${event.nbPlacesTotales - event.nbPlacesRestantes}" />
                    <c:if test="${reservees > 0}">
                        <div class="alert alert-warning">
                            Attention : ${reservees} place(s) déjà réservée(s). 
                            Vous ne pouvez pas réduire le nombre de places en dessous.
                        </div>
                    </c:if>
                </c:if>

                <form action="${pageContext.request.contextPath}/events/${editMode ? 'edit' : 'create'}" method="post">
                    <c:if test="${editMode}">
                        <input type="hidden" name="eventId" value="${event.id}">
                    </c:if>

                    <div class="form-group">
                        <label for="titre" class="form-label">Titre *</label>
                        <input type="text" class="form-control" id="titre" name="titre" 
                               value="${event.titre}" required>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="3">${event.description}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="dateEvenement" class="form-label">Date et heure *</label>
                        <input type="datetime-local" class="form-control" id="dateEvenement" name="dateEvenement" 
                               value="${event.dateEvenement}" required>
                    </div>

                    <div class="form-group">
                        <label for="lieu" class="form-label">Lieu *</label>
                        <input type="text" class="form-control" id="lieu" name="lieu" 
                               value="${event.lieu}" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="nbPlaces" class="form-label">Nombre de places *</label>
                            <input type="number" class="form-control" id="nbPlaces" name="nbPlaces" 
                                   value="${editMode ? event.nbPlacesTotales : ''}" min="1" required>
                        </div>

                        <div class="form-group">
                            <label for="prix" class="form-label">Prix (€) *</label>
                            <input type="number" class="form-control" id="prix" name="prix" 
                                   value="${event.prixBase}" min="0" step="0.01" required>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/events/my" class="btn btn-secondary">Annuler</a>
                        <button type="submit" class="btn btn-primary">
                            ${editMode ? 'Enregistrer' : 'Créer l\'événement'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
