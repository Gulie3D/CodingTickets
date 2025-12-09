<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${editMode ? 'Modifier' : 'Créer'} un événement - CodingTickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="mb-4">
                <h1 class="page-title">${editMode ? 'Modifier l\'événement' : 'Créer un événement'}</h1>
                <p class="page-subtitle">
                    ${editMode ? 'Mettez à jour les informations' : 'Remplissez les informations'}
                </p>
            </div>

            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">${sessionScope.error}</div>
                <c:remove var="error" scope="session"/>
            </c:if>
            
            <c:if test="${editMode && (event.nbPlacesTotales - event.nbPlacesRestantes) > 0}">
                <div class="alert alert-warning">
                    ⚠️ ${event.nbPlacesTotales - event.nbPlacesRestantes} place(s) déjà réservée(s).
                </div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/events/${editMode ? 'edit' : 'create'}" method="post">
                        
                        <c:if test="${editMode}">
                            <input type="hidden" name="eventId" value="${event.id}">
                        </c:if>
                        
                        <div class="mb-3">
                            <label for="titre" class="form-label">Titre *</label>
                            <input type="text" id="titre" name="titre" class="form-control" required
                                   value="${editMode ? event.titre : ''}" placeholder="Nom de l'événement">
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea id="description" name="description" class="form-control" rows="3"
                                      placeholder="Description">${editMode ? event.description : ''}</textarea>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dateEvenement" class="form-label">Date et heure *</label>
                                <input type="datetime-local" id="dateEvenement" name="dateEvenement" 
                                       class="form-control" required value="${editMode ? event.dateEvenement : ''}">
                                <small class="text-muted">Minimum : demain</small>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="lieu" class="form-label">Lieu *</label>
                                <input type="text" id="lieu" name="lieu" class="form-control" required
                                       value="${editMode ? event.lieu : ''}" placeholder="Adresse">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="nbPlaces" class="form-label">Nombre de places *</label>
                                <input type="number" id="nbPlaces" name="nbPlaces" class="form-control" required
                                       value="${editMode ? event.nbPlacesTotales : '50'}" 
                                       min="${editMode ? (event.nbPlacesTotales - event.nbPlacesRestantes) : 1}">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="prix" class="form-label">Prix (€) *</label>
                                <input type="number" id="prix" name="prix" class="form-control" required
                                       value="${editMode ? event.prixBase : '10.00'}" step="0.01" min="0">
                            </div>
                        </div>
                        
                        <div class="divider"></div>
                        
                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/events/my" class="btn btn-secondary">Annuler</a>
                            <button type="submit" class="btn btn-primary">
                                ${editMode ? 'Enregistrer' : 'Créer'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
