<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Créer un événement</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4>Créer un nouvel événement</h4>
                </div>
                <div class="card-body">

                    <c:if test="${not empty sessionScope.error}">
                        <div class="alert alert-danger">${sessionScope.error}</div>
                        <c:remove var="error" scope="session"/> </c:if>

                    <form action="${pageContext.request.contextPath}/events/create" method="post">
                        <div class="mb-3">
                            <label class="form-label">Titre</label>
                            <label>
                                <input type="text" name="titre" class="form-control" required>
                            </label>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <label>
                                <textarea name="description" class="form-control" rows="3"></textarea>
                            </label>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Date et Heure</label>
                                <label>
                                    <input type="datetime-local" name="dateEvenement" class="form-control" required>
                                </label>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Lieu</label>
                                <label>
                                    <input type="text" name="lieu" class="form-control" required>
                                </label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Nombre de places</label>
                                <label>
                                    <input type="number" name="nbPlaces" class="form-control" value="50" min="1" required>
                                </label>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Prix (€)</label>
                                <label>
                                    <input type="number" name="prix" class="form-control" value="10.00" step="0.01" min="0" required>
                                </label>
                            </div>
                        </div>
                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/events/my" class="btn btn-secondary">Annuler</a>
                            <button type="submit" class="btn btn-primary">Créer l'événement</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>