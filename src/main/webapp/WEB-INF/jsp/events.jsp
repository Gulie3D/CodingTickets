<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Événements - CodingTickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/events.css">
</head>
<body>

<nav class="navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/events">CodingTickets</a>
        
        <div class="navbar-actions">
            <c:if test="${not empty sessionScope.user}">
                <span class="user-info">${sessionScope.user.nom} (${sessionScope.user.role})</span>
                
                <c:if test="${sessionScope.user.role == 'CLIENT'}">
                    <a href="${pageContext.request.contextPath}/reservations/history" class="btn btn-outline-primary btn-sm">
                        Mes réservations
                    </a>
                </c:if>
                
                <c:if test="${sessionScope.user.role == 'ORGANISATEUR'}">
                    <a href="${pageContext.request.contextPath}/events/my" class="btn btn-outline-primary btn-sm">
                        Mes événements
                    </a>
                </c:if>
                
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Déconnexion</a>
            </c:if>
            
            <c:if test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">Connexion</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="page-container">
    <div class="page-header mb-4">
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h1 class="page-title">Événements disponibles</h1>
                <p class="page-subtitle">Découvrez et réservez vos places</p>
            </div>
            <c:if test="${sessionScope.user.role == 'ORGANISATEUR'}">
                <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary">
                    + Créer un événement
                </a>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <div class="row g-4">
        <c:forEach items="${events}" var="event">
            <div class="col-md-6 col-lg-4">
                <div class="card event-card">
                    <div class="card-body">
                        <div class="event-header">
                            <h5 class="event-title">${event.titre}</h5>
                            <span class="event-price">${event.prixBase}€</span>
                        </div>
                        
                        <div class="event-info">
                            <div class="event-info-item">${event.lieu}</div>
                            <div class="event-info-item">${event.dateFormatee}</div>
                        </div>
                        
                        <p class="event-description">${event.description}</p>
                        
                        <div class="event-footer">
                            <c:choose>
                                <c:when test="${event.nbPlacesRestantes == 0}">
                                    <span class="event-status full">Complet</span>
                                </c:when>
                                <c:otherwise>
                                    <div class="event-places">
                                        <span class="places-count">${event.nbPlacesRestantes}</span>
                                        <span class="places-label">places restantes</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            
                            <c:if test="${event.nbPlacesRestantes > 0 && sessionScope.user.role == 'CLIENT'}">
                                <form action="${pageContext.request.contextPath}/reservations/create" method="post" class="reserve-form">
                                    <input type="hidden" name="eventId" value="${event.id}">
                                    <input type="number" name="nbPlaces" value="1" min="1" max="${event.nbPlacesRestantes}" 
                                           class="form-control places-input">
                                    <button type="submit" class="btn btn-primary btn-sm">Réserver</button>
                                </form>
                            </c:if>
                            
                            <c:if test="${empty sessionScope.user}">
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-sm w-100">Connexion</a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    
    <c:if test="${empty events}">
        <div class="empty-state">
            <h5>Aucun événement disponible</h5>
            <p>Revenez plus tard pour découvrir de nouveaux événements.</p>
        </div>
    </c:if>
</div>

</body>
</html>
