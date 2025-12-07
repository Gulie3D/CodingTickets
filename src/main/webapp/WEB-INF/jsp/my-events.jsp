<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Mes événements organisés</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-dark mb-4">
    <div class="container">
        <span class="navbar-brand">Espace Organisateur</span>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/events/create" class="btn btn-success btn-sm">+ Créer</a>
            <a href="${pageContext.request.contextPath}/events" class="btn btn-outline-light btn-sm">Voir tout</a>
        </div>
    </div>
</nav>

<div class="container">

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">Action effectuée avec succès !</div>
    </c:if>

    <div class="card shadow">
        <div class="card-header bg-white">
            <h4 class="mb-0">Mes événements publiés</h4>
        </div>
        <div class="card-body p-0">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                <tr>
                    <th>Date</th>
                    <th>Titre</th>
                    <th>Lieu</th>
                    <th>Remplissage</th>
                    <th>Recette estimée</th>
                </tr>
                </thead>
                <tbody>

                <%-- Début de la boucle --%>
                <c:forEach items="${myEvents}" var="ev">
                    <tr>
                        <td>${ev.dateEvenement}</td>
                        <td>
                            <strong>${ev.titre}</strong><br>
                            <small class="text-muted">${ev.description}</small>
                        </td>
                        <td>${ev.lieu}</td>
                        <td>
                            <c:set var="reservees" value="${ev.nbPlacesTotales - ev.nbPlacesRestantes}" />

                            <div class="progress" style="height: 20px;">
                                <div class="progress-bar ${ev.nbPlacesRestantes == 0 ? 'bg-danger' : 'bg-success'}"
                                     role="progressbar"
                                     style="width: ${(reservees / ev.nbPlacesTotales) * 100}%">
                                        ${reservees}/${ev.nbPlacesTotales}
                                </div>
                            </div>
                            <small class="text-muted" style="font-size: 0.85em">
                                Reste : ${ev.nbPlacesRestantes} places
                            </small>
                        </td>
                        <td>
                            <span class="badge bg-primary fs-6">
                                ${(ev.nbPlacesTotales - ev.nbPlacesRestantes) * ev.prixBase} €
                            </span>
                            <div class="text-muted" style="font-size: 0.8em">Prix: ${ev.prixBase} €</div>
                        </td>
                    </tr>
                </c:forEach>
                <%-- Fin de la boucle (C'est cette balise qui manquait peut-être) --%>

                <%--@elvariable id="myEvents" type=""--%>
                <c:if test="${empty myEvents}">
                    <tr>
                        <td colspan="5" class="text-center py-5 text-muted">
                            Vous n'avez créé aucun événement pour l'instant.
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>