# Analyse des Routes API - ChâTop Backend

**Date**: 2025-10-07
**Source**: `ressources/mockoon/rental-oc.json`
**Endpoint Prefix**: `/api`
**Port Backend**: 3001

---

## 📋 Vue d'ensemble

**Nombre total de routes**: 9
**Routes publiques** (sans authentification): 2
**Routes protégées** (authentification JWT requise): 7

---

## 🔐 Routes d'Authentification

### 1. POST /api/auth/register

**Description**: Inscription d'un nouvel utilisateur

**Authentification**: ❌ Non requise

**Request Body** (Content-Type: application/json):
```json
{
  "email": "string (required)",
  "name": "string (required)",
  "password": "string (required)"
}
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Inscription réussie | `{ "token": "jwt" }` |
| 400 | Données manquantes (email, name ou password null) | `{}` |

**Spécificités**:
- Le mot de passe doit être crypté avec BCrypt avant stockage en BDD
- Génération d'un token JWT à retourner
- Validation des champs requis (email, name, password)
- Vérifier l'unicité de l'email

---

### 2. POST /api/auth/login

**Description**: Connexion d'un utilisateur existant

**Authentification**: ❌ Non requise

**Request Body** (Content-Type: application/json):
```json
{
  "email": "string (required)",
  "password": "string (required)"
}
```

**Exemple de credentials valides** (selon Mockoon):
- Email: `test@test.com`
- Password: `test!31`

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Connexion réussie | `{ "token": "jwt" }` |
| 401 | Identifiants invalides | `{ "message": "error" }` |

**Spécificités**:
- Vérifier l'email et le mot de passe avec BCrypt
- Génération d'un token JWT à retourner
- Gestion des erreurs d'authentification

---

### 3. GET /api/auth/me

**Description**: Récupérer les informations de l'utilisateur connecté

**Authentification**: ✅ Requise (Bearer Token)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Utilisateur authentifié | Voir format ci-dessous |
| 401 | Token manquant ou invalide | `{}` |

**Format de réponse 200**:
```json
{
  "id": 1,
  "name": "Test TEST",
  "email": "test@test.com",
  "created_at": "2022/02/02",
  "updated_at": "2022/08/02"
}
```

**Spécificités**:
- Extraire l'utilisateur à partir du token JWT
- Format de date: `yyyy/MM/dd`
- Ne pas retourner le mot de passe

---

## 🏠 Routes Rentals (Locations)

### 4. GET /api/rentals

**Description**: Récupérer la liste de toutes les locations

**Authentification**: ✅ Requise (Bearer Token)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Liste des locations | Voir format ci-dessous |
| 401 | Token manquant ou invalide | `""` (empty) |

**Format de réponse 200**:
```json
{
  "rentals": [
    {
      "id": 1,
      "name": "test house 1",
      "surface": 432,
      "price": 300,
      "picture": "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg",
      "description": "Lorem ipsum dolor sit amet...",
      "owner_id": 1,
      "created_at": "2012/12/02",
      "updated_at": "2014/12/02"
    },
    {
      "id": 2,
      "name": "test house 2",
      "surface": 154,
      "price": 200,
      "picture": "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg",
      "description": "Lorem ipsum dolor sit amet...",
      "owner_id": 2,
      "created_at": "2012/12/02",
      "updated_at": "2014/12/02"
    }
  ]
}
```

**Spécificités**:
- La réponse est enveloppée dans un objet avec clé `rentals`
- Format de date: `yyyy/MM/dd`
- `picture` est une string (URL)
- `surface` est un nombre entier
- `price` est un nombre entier

---

### 5. GET /api/rentals/:id

**Description**: Récupérer les détails d'une location spécifique

**Authentification**: ✅ Requise (Bearer Token)

**Path Parameters**:
- `id`: Integer (ID de la location)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Détails de la location | Voir format ci-dessous |
| 401 | Token manquant ou invalide | `""` (empty) |
| 404 | Location non trouvée | À implémenter |

**Format de réponse 200**:
```json
{
  "id": 1,
  "name": "dream house",
  "surface": 24,
  "price": 30,
  "picture": ["https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg"],
  "description": "Lorem ipsum dolor sit amet...",
  "owner_id": 1,
  "created_at": "2012/12/02",
  "updated_at": "2014/12/02"
}
```

**⚠️ Différence importante avec GET /api/rentals**:
- `picture` est un **array de strings** dans le détail (au lieu d'une simple string dans la liste)

**Spécificités**:
- L'objet n'est **pas** enveloppé (contrairement à GET /api/rentals)
- Format de date: `yyyy/MM/dd`
- Attention au type de `picture`: array vs string selon le endpoint

---

### 6. POST /api/rentals

**Description**: Créer une nouvelle location

**Authentification**: ✅ Requise (Bearer Token)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Request Body** (Content-Type: multipart/form-data):
```
name: string (required)
surface: number (required)
price: number (required)
picture: file (required) - image upload
description: string (required)
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Location créée avec succès | `{ "message": "Rental created !" }` |
| 401 | Token manquant ou invalide | `""` (empty) |
| 400 | Données manquantes ou invalides | À implémenter |

**Spécificités**:
- Upload d'image via multipart/form-data
- Récupérer l'`owner_id` à partir du token JWT
- Stocker l'image sur le serveur et sauvegarder le chemin/URL en BDD
- Générer automatiquement `created_at` et `updated_at`
- Valider tous les champs requis

---

### 7. PUT /api/rentals/:id

**Description**: Mettre à jour une location existante

**Authentification**: ✅ Requise (Bearer Token)

**Path Parameters**:
- `id`: Integer (ID de la location à modifier)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Request Body** (Content-Type: multipart/form-data):
```
name: string (optional)
surface: number (optional)
price: number (optional)
description: string (optional)
picture: file (optional) - nouvelle image
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Location mise à jour avec succès | `{ "message": "Rental updated !" }` |
| 401 | Token manquant ou invalide | `""` (empty) |
| 404 | Location non trouvée | À implémenter |
| 403 | L'utilisateur n'est pas le propriétaire | À implémenter (optionnel) |

**Spécificités**:
- Vérifier que la location existe
- Optionnel: Vérifier que l'utilisateur connecté est le propriétaire (`owner_id`)
- Si nouvelle image uploadée, remplacer l'ancienne
- Mettre à jour `updated_at` automatiquement
- Ne mettre à jour que les champs fournis

---

## 💬 Route Messages

### 8. POST /api/messages

**Description**: Envoyer un message concernant une location

**Authentification**: ✅ Requise (Bearer Token)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Request Body** (Content-Type: application/json):
```json
{
  "message": "string (required)",
  "user_id": "number (required)",
  "rental_id": "number (required)"
}
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Message envoyé avec succès | `{ "message": "Message send with success" }` |
| 400 | Données manquantes (message, user_id ou rental_id null) | `{}` |
| 401 | Token manquant ou invalide | `""` (empty) |

**Spécificités**:
- Valider que `rental_id` existe
- Valider que `user_id` existe
- Générer automatiquement `created_at` et `updated_at`
- Stocker le message en BDD

---

## 👤 Route Utilisateurs

### 9. GET /api/user/:id

**Description**: Récupérer les informations d'un utilisateur par son ID

**Authentification**: ✅ Requise (Bearer Token)

**Path Parameters**:
- `id`: Integer (ID de l'utilisateur)

**Headers requis**:
```
Authorization: Bearer {jwt_token}
```

**Réponses**:

| Code | Description | Body |
|------|-------------|------|
| 200 | Informations de l'utilisateur | Voir format ci-dessous |
| 401 | Token manquant ou invalide | `""` (empty) |
| 404 | Utilisateur non trouvé | À implémenter |

**Format de réponse 200**:
```json
{
  "id": 2,
  "name": "Owner Name",
  "email": "test@test.com",
  "created_at": "2022/02/02",
  "updated_at": "2022/08/02"
}
```

**Spécificités**:
- Ne pas retourner le mot de passe
- Format de date: `yyyy/MM/dd`
- Utilisé pour afficher les infos du propriétaire d'une location

---

## 🔧 Dépendances Spring Boot Nécessaires

### 1. Spring Boot Starter Web
**Artefact**: `spring-boot-starter-web`
**Utilité**: Controllers REST, gestion HTTP, Jackson pour JSON

### 2. Spring Boot Starter Data JPA
**Artefact**: `spring-boot-starter-data-jpa`
**Utilité**: Repositories, gestion des entités, accès aux données

### 3. MySQL Connector
**Artefact**: `mysql-connector-j` (ou `mysql-connector-java`)
**Utilité**: Driver JDBC pour MySQL

### 4. Spring Boot Starter Security
**Artefact**: `spring-boot-starter-security`
**Utilité**: Sécurité, authentification, autorisation

### 5. JSON Web Token (JWT)
**Artefact**: `jjwt`
**Utilité**: Génération et validation des tokens JWT


### 6. Spring Boot Starter Validation
**Artefact**: `spring-boot-starter-validation`
**Utilité**: Validation des données (annotations @NotNull, @NotBlank, etc.)


### 7. Swagger/OpenAPI Documentation
**Artefact**: `springdoc-openapi-starter-webmvc-ui` (Spring Boot 3.x)
ou `springdoc-openapi-ui` (Spring Boot 2.x)
**Utilité**: Documentation automatique de l'API

---

## 🎯 Points Clés à Retenir

### Sécurité
1. **2 routes publiques**: `/api/auth/register` et `/api/auth/login`
2. **7 routes protégées**: Toutes les autres requièrent un Bearer Token
3. Format du header d'authentification: `Authorization: Bearer {jwt_token}`
4. Réponse 401 si token manquant ou invalide

### Format des Données
1. **Dates**: Format `yyyy/MM/dd` (exemple: `2022/02/02`)
2. **Content-Type**: `application/json` pour la plupart des routes
3. **Exception**: Upload d'images → `multipart/form-data`

### Spécificités Importantes
1. **GET /api/rentals** → `picture` est une **string**
2. **GET /api/rentals/:id** → `picture` est un **array**
3. **GET /api/rentals** → Réponse enveloppée dans `{ "rentals": [...] }`
4. **GET /api/rentals/:id** → Réponse **non enveloppée**

### Messages de Succès
- Création rental: `"Rental created !"`
- Mise à jour rental: `"Rental updated !"`
- Envoi message: `"Message send with success"`

### Gestion des Erreurs
- **400**: Données manquantes ou invalides → `{}`
- **401**: Authentification échouée ou token invalide → `{}` ou `""`
- **404**: Ressource non trouvée → À implémenter

---

## 📝 Recommandations pour l'Implémentation

### Architecture
1. Créer les **Entities** (User, Rental, Message)
2. Créer les **Repositories** (UserRepository, RentalRepository, MessageRepository)
3. Créer les **DTOs** pour chaque réponse (UserDTO, RentalDTO, etc.)
4. Créer les **Services** avec la logique métier
5. Créer les **Controllers** pour exposer les routes
6. Configurer **Spring Security** avec JWT
7. Implémenter un **JwtAuthenticationFilter**
8. Documenter avec **Swagger**

### Phases d'implémentation
1. **Phase 1**: Authentification
   - POST /api/auth/register
   - POST /api/auth/login
   - Configuration Spring Security + JWT
   - GET /api/auth/me

2. **Phase 2**: Rentals (lecture)
   - GET /api/rentals
   - GET /api/rentals/:id

3. **Phase 3**: Rentals (écriture)
   - POST /api/rentals (avec upload d'image)
   - PUT /api/rentals/:id

4. **Phase 4**: Messages et Users
   - POST /api/messages
   - GET /api/user/:id

5. **Phase 5**: Finalisation
   - Documentation Swagger
   - Tests
   - Gestion d'erreurs
