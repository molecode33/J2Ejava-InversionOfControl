# Projet Java - Inversion de Contrôle et Injection de Dépendances

## 📋 Description

Ce projet a été développé dans un but pédagogique pour comprendre et maîtriser les concepts fondamentaux de l'**Inversion de Contrôle (IoC)** et de l'**Injection de Dépendances (DI)** **sans utiliser de framework**.

L'approche adoptée consiste à implémenter manuellement les mécanismes d'injection de dépendances pour bien comprendre les principes sous-jacents avant de passer à l'utilisation de frameworks comme Spring ou CDI.

## 🎯 Objectifs d'apprentissage

- Comprendre le principe de l'Inversion de Contrôle de manière concrète
- Maîtriser l'injection par constructeur et par setter
- Implémenter une architecture en couches (DAO, Métier, Présentation)
- Réduire le couplage entre les composants
- Faciliter la testabilité et la maintenabilité du code
- Poser les bases avant l'utilisation de frameworks

## 🔑 Concepts clés

### Inversion de Contrôle (IoC)

L'IoC est un principe de conception où le contrôle du flux d'exécution est inversé : au lieu que le code appelant crée et gère ses dépendances, c'est un conteneur ou un framework qui s'en charge.

### Injection de Dépendances (DI)

La DI est une implémentation de l'IoC où les dépendances d'un objet sont fournies de l'extérieur plutôt que créées par l'objet lui-même.

#### Types d'injection

1. **Injection statique** : Les dépendances sont codées en dur dans le code source (nécessite recompilation pour changer)
2. **Injection dynamique** : Les dépendances sont définies dans un fichier de configuration et injectées via Reflection (aucune recompilation nécessaire)

#### Méthodes d'injection implémentées

1. **Injection par constructeur** : Les dépendances sont passées via le constructeur lors de l'instanciation
2. **Injection par setter** : Les dépendances sont définies via des méthodes setter après la création de l'objet
3. **Injection par annotation** : Utilisée par les frameworks (Spring, CDI) - non implémentée dans ce projet car on se concentre sur les fondamentaux

**Note** : Ce projet utilise les deux premiers types (constructeur et setter) avec les deux modes (statique et dynamique)

## 🛠️ Technologies utilisées

- Java SE 17
- Architecture 3 couches
- Maven 

## 📁 Structure du projet

```
src/main/java
        ├── dao/                    # Couche DAO (Data Access Object)
        │   ├── IDao.java          # Interface DAO
        │   └── DaoImpl.java       # Implémentation DAO
        ├── metier/                # Couche Métier (Business Logic)
        │   ├── IMetier.java       # Interface Métier
        │   └── MetierImpl.java    # Implémentation Métier
        └── presentation/          # Couche Présentation
            └── Presentation.java          # Point d'entrée de l'application
```

## 🏗️ Architecture en couches

### Couche DAO (Data Access Object)
Responsable de l'accès aux données. Cette couche gère la récupération et la persistance des données.

### Couche Métier
Contient la logique métier de l'application. Elle utilise la couche DAO pour accéder aux données et applique les règles de gestion.

### Couche Présentation
Gère l'interaction avec l'utilisateur et l'affichage des résultats. Elle utilise la couche Métier pour effectuer les traitements.

## 🔌 Couplage faible avec les interfaces

### Principe fondamental

L'utilisation d'**interfaces** est au cœur de cette architecture et permet d'obtenir un **couplage faible** entre les différentes couches.

### Qu'est-ce que le couplage faible ?

Le couplage faible signifie que les composants de l'application dépendent d'**abstractions** (interfaces) plutôt que d'**implémentations concrètes** (classes).

#### Exemple de couplage fort (❌ À éviter)

```java
public class MetierImpl {
    // Dépendance directe à une implémentation concrète
    private DaoImpl dao = new DaoImpl();
    
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Problèmes** :
- `MetierImpl` est directement liée à `DaoImpl`
- Impossible de changer l'implémentation sans modifier `MetierImpl`
- Difficile à tester (on ne peut pas utiliser une fausse DAO)
- Violation du principe ouvert/fermé

#### Exemple de couplage faible (✅ Bonne pratique)

```java
public class MetierImpl implements IMetier {
    // Dépendance à une interface, pas à une classe concrète
    private IDao dao;
    
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }
    
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Avantages** :
- `MetierImpl` dépend de l'interface `IDao`, pas d'une implémentation
- On peut fournir n'importe quelle implémentation de `IDao`
- Facile à tester avec des implémentations de test
- Respect du principe ouvert/fermé

### Schéma de l'architecture

```
┌─────────────────────┐
│   Présentation      │
│   (Main.java)       │
└──────────┬──────────┘
           │ dépend de
           ▼
┌─────────────────────┐         ┌─────────────────────┐
│    <<interface>>    │◄--------┤   MetierImpl        │
│      IMetier        │         │                     │
└─────────────────────┘         └──────────┬──────────┘
                                           │ dépend de
                                           ▼
                                ┌─────────────────────┐         ┌─────────────────────┐
                                │    <<interface>>    │◄--------┤    DaoImpl          │
                                │       IDao          │         │                     │
                                └─────────────────────┘         └─────────────────────┘
```

### Avantages du couplage faible

#### 1. Flexibilité et évolutivité
On peut facilement créer plusieurs implémentations :

```java
// Implémentation avec base de données
public class DaoImplDB implements IDao {
    @Override
    public double getData() {
        // Connexion à une base de données
        return // données de la BD
    }
}

// Implémentation avec fichier
public class DaoImplFile implements IDao {
    @Override
    public double getData() {
        // Lecture depuis un fichier
        return // données du fichier
    }
}

// Implémentation avec Web Service
public class DaoImplWS implements IDao {
    @Override
    public double getData() {
        // Appel à un web service
        return // données du WS
    }
}
```

Et dans la présentation, on choisit simplement :

```java
// On peut changer l'implémentation sans toucher au code métier
IDao dao = new DaoImplDB();        // ou
IDao dao = new DaoImplFile();      // ou
IDao dao = new DaoImplWS();

IMetier metier = new MetierImpl(dao);
```

#### 2. Testabilité
Création facile d'implémentations pour les tests :

```java
public class DaoImplMock implements IDao {
    @Override
    public double getData() {
        return 100.0; // Valeur de test contrôlée
    }
}

// Dans les tests
IDao daoTest = new DaoImplMock();
IMetier metier = new MetierImpl(daoTest);
// Tests avec des données prévisibles
```

#### 3. Maintenabilité
Si on change l'implémentation de la DAO, le code de la couche métier **ne change pas** :

```java
// Avant
IDao dao = new DaoImplFile();

// Après : passage à une base de données
IDao dao = new DaoImplDB();

// La couche métier ne change pas !
IMetier metier = new MetierImpl(dao);
```

#### 4. Principe de ségrégation des interfaces
Chaque couche ne voit que ce dont elle a besoin :

```java
// La couche métier ne connaît que ces méthodes
public interface IDao {
    double getData();
}

// Elle ne voit pas les détails d'implémentation
public class DaoImpl implements IDao {
    private Connection connection; // Invisible pour la couche métier
    private void openConnection() { } // Invisible
    private void closeConnection() { } // Invisible
    
    @Override
    public double getData() {
        // Seule cette méthode est exposée
    }
}
```

### Respect des principes SOLID

Cette architecture respecte plusieurs principes SOLID :

- **S** - Single Responsibility : Chaque couche a une responsabilité unique
- **O** - Open/Closed : Ouvert à l'extension (nouvelles implémentations), fermé à la modification
- **L** - Liskov Substitution : Toute implémentation de `IDao` peut remplacer une autre
- **I** - Interface Segregation : Interfaces minimales et spécifiques
- **D** - Dependency Inversion : Dépendance aux abstractions, pas aux détails

## 🔧 Maintenabilité de l'application

### Principe Open/Closed (Ouvert/Fermé)

L'un des principes fondamentaux d'une application maintenable est le **principe Open/Closed** :

> *"Une application doit être fermée à la modification et ouverte à l'extension"*

Cela signifie qu'on doit pouvoir **ajouter de nouvelles fonctionnalités sans modifier le code existant**.

### Comparaison : Couplage fort vs Couplage faible

#### ❌ Avec couplage fort : Fermé à l'extension, ouvert à la modification

```java
public class MetierImpl {
    private DaoImplFile dao = new DaoImplFile();
    
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Problème** : Pour changer de source de données (passer d'un fichier à une base de données) :

```java
import metier.IMetier;

public class MetierImpl implements IMetier {
    // ❌ Il faut MODIFIER cette ligne
    private DaoImplDB dao = new DaoImplDB(); // Changement de DaoImplFile à DaoImplDB

    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Conséquences négatives** :
- Modification du code source existant
- Risque d'introduire des bugs dans du code qui fonctionnait
- Nécessité de recompiler la classe `MetierImpl`
- Nécessité de retester toute la couche métier
- Violation du principe Open/Closed
- Maintenance coûteuse et risquée

#### ✅ Avec couplage faible : Ouvert à l'extension, fermé à la modification

```java
public class MetierImpl implements IMetier {
    private IDao dao;
    
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }
    
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Solution** : Pour changer de source de données, on crée une nouvelle implémentation :

```java
// ✅ On AJOUTE une nouvelle classe sans toucher aux existantes
public class DaoImplDB implements IDao {
    @Override
    public double getData() {
        // Récupération depuis base de données
        return // données de la BD
    }
}
```

Et dans la présentation :

```java
// Avant : utilisation d'un fichier
IDao dao = new DaoImplFile();
IMetier metier = new MetierImpl(dao);

// Après : passage à une base de données
// ✅ Aucune modification dans MetierImpl !
IDao dao = new DaoImplDB();
IMetier metier = new MetierImpl(dao); // Même code métier
```

⚠️ **Attention** : Cette approche nécessite encore de modifier `Presentation.java`. Pour une application **vraiment** fermée à la modification, il faut utiliser l'**injection dynamique** (voir section suivante).

**Avantages** :
- **Aucune modification** du code de la couche métier
- Le code existant reste intact et stable
- Pas de risque d'introduire des bugs dans `MetierImpl`
- Pas besoin de recompiler `MetierImpl`
- Extension facile par ajout de nouvelles classes
- Respect du principe Open/Closed pour la couche métier
- Maintenance simple et sûre

### Scénarios concrets de maintenabilité

#### Scénario 1 : Changement de source de données

**Besoin** : L'application utilisait des fichiers, maintenant elle doit utiliser une base de données.

**Avec injection dynamique (✅ Vraiment fermée à la modification)** :
1. Créer `DaoImplDB implements IDao` (nouvelle classe)
2. Modifier uniquement le fichier `config.txt` : `dao.DaoImplDB`
3. **Aucune ligne de code modifiée** → aucun risque de régression

**Avec injection statique (⚠️ Encore ouverte à la modification)** :
1. Créer `DaoImplDB implements IDao` (nouvelle classe)
2. Modifier `Main.java` : `IDao dao = new DaoImplDB();`
3. Recompiler `Main.java`
4. `MetierImpl` ne change pas (bon point), mais `Main.java` change (pas idéal)

**Avec couplage fort (❌ Ouvert à la modification)** :
1. Modifier `MetierImpl` pour remplacer `DaoImplFile` par `DaoImplDB`
2. Recompiler `MetierImpl`
3. Retester toute la couche métier
4. Risque de bugs

#### Scénario 2 : Ajout d'une source de données alternative

**Besoin** : Ajouter la possibilité de récupérer les données depuis un Web Service.

**Avec injection dynamique (✅ Vraiment fermée)** :
```java
// ✅ Simple ajout d'une nouvelle classe
public class DaoImplWebService implements IDao {
    @Override
    public double getData() {
        // Appel au Web Service
        return // données du WS
    }
}
```

Dans `config.txt` :
```
dao.DaoImplWebService
metier.MetierImpl
```

**Impact** : Zéro ligne de code modifiée ! Juste ajout d'une classe et modification du fichier de configuration.

**Avec injection statique (⚠️ Modification nécessaire)** :
```java
// Dans Main.java - il faut modifier cette ligne
IDao dao = new DaoImplWebService(); // Modification du code source
IMetier metier = new MetierImpl(dao);
```

**Avec couplage fort (❌ Pire cas)** :
```java
// ❌ Il faudrait modifier MetierImpl
public class MetierImpl {
    private DaoImplWebService dao = new DaoImplWebService();
    // Modification du code existant → risque
}
```

#### Scénario 3 : Plusieurs implémentations métier

**Besoin** : Ajouter une nouvelle règle de calcul métier.

**Avec couplage faible** :
```java
// ✅ Ajout d'une nouvelle implémentation
public class MetierImplV2 implements IMetier {
    private IDao dao;
    
    public MetierImplV2(IDao dao) {
        this.dao = dao;
    }
    
    @Override
    public double calcul() {
        return dao.getData() * 3; // Nouvelle règle
    }
}

// Dans Main.java - Choix de l'implémentation
IMetier metier = new MetierImplV2(dao); // Nouvelle version
// ou
IMetier metier = new MetierImpl(dao);   // Ancienne version
```

**Impact** : Zéro modification du code existant, juste ajout d'une nouvelle classe.

### Tableau comparatif

| Critère | Couplage Fort | Injection Statique | Injection Dynamique |
|---------|---------------|-------------------|---------------------|
| **Modification du code existant** | ❌ Fréquente (MetierImpl) | ⚠️ Limitée (Main.java) | ✅ Aucune |
| **Recompilation nécessaire** | ❌ Oui (MetierImpl) | ⚠️ Oui (Main.java) | ✅ Non |
| **Modification du fichier config** | ❌ N/A | ❌ N/A | ✅ Oui (config.txt) |
| **Ajout de nouvelles fonctionnalités** | ❌ Nécessite modification | ⚠️ Nécessite modification de Main | ✅ Simple ajout de classes |
| **Risque de régression** | ❌ Élevé | ⚠️ Moyen | ✅ Très faible |
| **Temps de maintenance** | ❌ Long | ⚠️ Moyen | ✅ Court |
| **Coût de maintenance** | ❌ Élevé | ⚠️ Moyen | ✅ Faible |
| **Respect Open/Closed complet** | ❌ Non | ⚠️ Partiel (MetierImpl fermé) | ✅ Oui (toutes couches) |
| **Stabilité du code** | ❌ Faible | ⚠️ Moyenne | ✅ Élevée |
| **Testabilité** | ❌ Difficile | ✅ Facile | ✅ Facile |
| **Déploiement à chaud** | ❌ Non | ❌ Non | ✅ Possible |

### Exigences techniques et qualité logicielle

Dans un projet professionnel, la maintenabilité est une **exigence technique critique** :

**Pourquoi c'est important** :
- **Coût** : 60-80% du coût d'un logiciel est lié à la maintenance
- **Évolution** : Les besoins changent constamment
- **Équipe** : Le code doit être compréhensible et modifiable par d'autres développeurs
- **Pérennité** : L'application doit vivre plusieurs années

**Ce que permet le couplage faible** :
- ✅ Extensions sans modifications dangereuses
- ✅ Ajout de fonctionnalités sans risque
- ✅ Code stable et prévisible
- ✅ Réduction des coûts de maintenance
- ✅ Facilite le travail en équipe
- ✅ Meilleure qualité logicielle globale

### Conclusion sur la maintenabilité

Le **couplage faible** grâce aux **interfaces** et à l'**injection de dépendances** transforme une application rigide en une application flexible et évolutive.

**Trois niveaux de maintenabilité** :

1. **Couplage fort** (❌ Pire cas) : Chaque modification touche plusieurs classes, risque élevé
2. **Injection statique** (⚠️ Mieux) : La couche métier est fermée, mais la couche présentation est encore ouverte à la modification
3. **Injection dynamique** (✅ Idéal) : **Toutes les couches sont fermées à la modification**, seul le fichier de configuration change

**L'injection dynamique** est la solution qui respecte **vraiment** le principe Open/Closed car :
- ✅ Aucune ligne de code Java n'est modifiée lors d'une extension
- ✅ Aucune recompilation nécessaire
- ✅ Changement de comportement par simple modification de configuration
- ✅ C'est exactement ce principe que les frameworks comme Spring et CDI automatisent

## 🚀 Installation et exécution

### Prérequis

- JDK 8 ou supérieur
- Un IDE Java (Eclipse, IntelliJ IDEA, NetBeans)

### Étapes d'exécution

```bash
# Compiler les fichiers Java
javac src/**/*.java

# Exécuter l'application
java src/presentation/Main
```

Ou simplement ouvrir le projet dans votre IDE et exécuter la classe `Main`.

## 💡 Exemples de code

### Structure des interfaces et classes

#### Couche DAO

```java
// Interface DAO
public interface IDao {
    double getData();
}

// Implémentation DAO
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        // Simulation de récupération de données
        System.out.println("Récupération des données depuis la base...");
        return 123.45;
    }
}
```

#### Couche Métier

```java
// Interface Métier
public interface IMetier {
    double calcul();
}

// Implémentation Métier
public class MetierImpl implements IMetier {
    // Dépendance vers la couche DAO
    private IDao dao;
    
    // Constructeur par défaut
    public MetierImpl() {
    }
    
    // Constructeur pour injection par constructeur
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }
    
    // Setter pour injection par setter
    public void setDao(IDao dao) {
        this.dao = dao;
    }
    
    @Override
    public double calcul() {
        double data = dao.getData();
        return data * 2; // Exemple de traitement métier
    }
}
```

### Injection statique vs Injection dynamique

La distinction fondamentale n'est pas entre constructeur et setter, mais entre **injection statique** et **injection dynamique**.

#### ⚠️ Injection statique : Encore ouverte à la modification

L'**injection statique** signifie que les implémentations concrètes sont **codées en dur** dans le code source, que ce soit par constructeur ou par setter.

**Exemple avec injection par constructeur (statique)** :

```java
public class Main {
    public static void main(String[] args) {
        // ⚠️ Problème : si on veut changer d'implémentation,
        // il faut MODIFIER ce code source
        IDao dao = new DaoImpl();
        IMetier metier = new MetierImpl(dao);
        
        System.out.println("Résultat : " + metier.calcul());
    }
}
```

**Limitation** : Pour passer de `DaoImpl` à `DaoImplV2`, il faut :
1. Modifier le code source de `Main.java`
2. Recompiler l'application
3. Redéployer

❌ La couche présentation **n'est pas fermée à la modification**

#### ✅ Injection dynamique : Vraiment fermée à la modification

L'**injection dynamique** permet de définir les implémentations dans un **fichier de configuration externe** et d'utiliser la **Reflection** pour instancier et injecter les dépendances. Cela fonctionne avec n'importe quelle méthode d'injection (constructeur ou setter).

Pour avoir une application **complètement fermée à la modification et ouverte à l'extension**, on utilise l'**injection dynamique** avec un fichier de configuration.

**Fichier de configuration `config.txt`** :
```
dao.DaoImpl
metier.MetierImpl
```

**Code de la couche présentation avec injection dynamique par setter** :

```java
import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Injection dynamique qui permet à l'application d'être 
        // complètement fermée à la modification et ouverte à l'extension
        Scanner sc = new Scanner(new File("config.txt"));
        
        // Lecture du nom de la classe DAO depuis le fichier
        String daoClassName = sc.nextLine();
        
        // Instanciation dynamique de la DAO via Reflection
        Class<?> cDao = Class.forName(daoClassName);
        IDao dao = (IDao) cDao.getConstructor().newInstance();
        System.out.println("Données : " + dao.getData());
        
        // Lecture du nom de la classe Métier depuis le fichier
        String metierClassName = sc.nextLine();
        
        // Instanciation dynamique de la couche Métier via Reflection
        Class<?> cMetier = Class.forName(metierClassName);
        IMetier metier = (IMetier) cMetier.getConstructor().newInstance();
        
        // Injection de la dépendance via Reflection (appel du setter)
        Method setDao = cMetier.getDeclaredMethod("setDao", IDao.class);
        setDao.invoke(metier, dao);
        
        // Utilisation
        System.out.println("Résultat = " + metier.calcul());
        
        sc.close();
    }
}
```

**Avantages de l'injection dynamique** :

✅ **Zéro modification du code source** : Pour changer d'implémentation, on modifie uniquement `config.txt`

✅ **Pas de recompilation** : Le bytecode reste inchangé

✅ **Déploiement à chaud** : On peut changer de comportement sans redémarrer

✅ **Vraiment Open/Closed** : Ajout de nouvelles implémentations sans toucher au code

**Exemple d'extension** :

Si on crée une nouvelle implémentation `DaoImplV2` :

```java
public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        return 999.99;
    }
}
```

Pour l'utiliser, on modifie **uniquement** `config.txt` :
```
dao.DaoImplV2
metier.MetierImpl
```

❌ **Aucune ligne de code modifiée** dans `Main.java` !

### Injection par constructeur avec Reflection (dynamique)

On peut aussi faire l'injection par constructeur de manière dynamique :

```java
// Lecture de la configuration
Scanner sc = new Scanner(new File("config.txt"));
String daoClassName = sc.nextLine();
String metierClassName = sc.nextLine();

// Instanciation de la DAO
Class<?> cDao = Class.forName(daoClassName);
IDao dao = (IDao) cDao.getConstructor().newInstance();

// Instanciation de la couche Métier avec injection par constructeur (dynamique)
Class<?> cMetier = Class.forName(metierClassName);
IMetier metier = (IMetier) cMetier.getConstructor(IDao.class).newInstance(dao);

System.out.println("Résultat = " + metier.calcul());
```

**Note** : La **méthode d'injection** (constructeur ou setter) est différente du **type d'injection** (statique ou dynamique). On peut combiner :
- Injection par constructeur statique
- Injection par constructeur dynamique
- Injection par setter statique
- Injection par setter dynamique

### Comparaison des approches

| Approche | Type | Méthode | Modification du code | Recompilation | Vraiment Open/Closed |
|----------|------|---------|---------------------|---------------|---------------------|
| **new DaoImpl()** dans Main | Statique | Manuelle | ❌ Oui (Main.java) | ❌ Oui | ❌ Non |
| **Injection par constructeur** dans Main | Statique | Constructeur | ❌ Oui (Main.java) | ❌ Oui | ❌ Non |
| **Injection par setter** dans Main | Statique | Setter | ❌ Oui (Main.java) | ❌ Oui | ❌ Non |
| **Reflection + config + constructeur** | Dynamique | Constructeur | ✅ Non (juste config.txt) | ✅ Non | ✅ Oui |
| **Reflection + config + setter** | Dynamique | Setter | ✅ Non (juste config.txt) | ✅ Non | ✅ Oui |
| **Frameworks (Spring, CDI)** | Dynamique | Annotation | ✅ Non (juste config/annotations) | ✅ Non | ✅ Oui |

### Exemple 1 : Injection par constructeur (statique)

```java
public class Main {
    public static void main(String[] args) {
        // Instanciation de la couche DAO
        IDao dao = new DaoImpl();
        
        // Injection par constructeur (statique)
        IMetier metier = new MetierImpl(dao);
        
        // Utilisation
        System.out.println("Résultat : " + metier.calcul());
    }
}
```

**Type** : Injection statique  
**Méthode** : Constructeur

**Avantages** :
- Les dépendances sont obligatoires et définies à la création
- L'objet est dans un état cohérent dès son instanciation
- Facilite l'immutabilité

**Inconvénient** :
- ⚠️ Nécessite de modifier `Presentation.java` pour changer d'implémentation (pas fermé à la modification)

### Exemple 2 : Injection par setter (statique)

```java
public class Presentation {
    public static void main(String[] args) {
        // Instanciation de la couche DAO
        IDao dao = new DaoImpl();
        
        // Instanciation de la couche métier
        MetierImpl metier = new MetierImpl();
        
        // Injection par setter (statique)
        metier.setDao(dao);
        
        // Utilisation
        System.out.println("Résultat : " + metier.calcul());
    }
}
```

**Type** : Injection statique  
**Méthode** : Setter

**Avantages** :
- Flexibilité : possibilité de changer la dépendance après création
- Dépendances optionnelles possibles

**Inconvénient** :
- ⚠️ Nécessite de modifier `Presentation.java` pour changer d'implémentation (pas fermé à la modification)

### Comparaison sans injection de dépendances

```java
// ❌ Mauvaise pratique : Couplage fort
public class MetierImpl implements IMetier {
    private IDao dao = new DaoImpl(); // Dépendance créée directement
    
    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}
```

**Problèmes** :
- Couplage fort entre les couches
- Difficile à tester (impossible de mocker la DAO)
- Pas de flexibilité pour changer l'implémentation
- Modification du code nécessaire pour changer de DAO

### Ce que simule l'injection dynamique

L'injection dynamique avec Reflection simule ce que font les frameworks comme **Spring** ou **CDI** :
- Lecture de configuration (XML, annotations, fichiers properties)
- Instanciation automatique des classes
- Injection automatique des dépendances
- Gestion du cycle de vie des objets

C'est exactement le principe qu'utilisent ces frameworks, mais de manière beaucoup plus sophistiquée !

**Les 3 méthodes d'injection que les frameworks utilisent** :
1. **Injection par constructeur** : `@Autowired` sur le constructeur (Spring) ou constructeur avec `@Inject` (CDI)
2. **Injection par setter** : `@Autowired` sur le setter (Spring) ou `@Inject` sur le setter (CDI)
3. **Injection par annotation sur le champ** : `@Autowired` ou `@Inject` directement sur l'attribut (non implémenté dans ce projet car nous nous concentrons sur les fondamentaux)

## 🎓 Concepts appris

### Avantages de l'injection de dépendances

- **Découplage** : Les couches ne dépendent que des interfaces, pas des implémentations concrètes
- **Testabilité** : Possibilité de créer des implémentations de test (mocks) facilement
- **Maintenabilité** : Changement d'implémentation sans modifier le code client
- **Réutilisabilité** : Les composants peuvent être réutilisés avec différentes implémentations
- **Flexibilité** : Possibilité de changer de comportement à l'exécution

### Principe d'inversion de contrôle

Au lieu que la couche Métier crée elle-même sa dépendance vers la DAO, c'est la couche Présentation qui contrôle et injecte cette dépendance. Le contrôle est donc "inversé".

### Bonnes pratiques appliquées

- Programmation orientée interfaces
- Respect du principe de responsabilité unique
- Séparation des préoccupations (Separation of Concerns)
- Architecture en couches

## Évolutions possibles

- Ajouter une configuration externe (fichier de configuration)
- Implémenter un conteneur IoC simple
- Ajouter plusieurs implémentations de DAO (base de données, fichiers, web service)
- Créer une version avec annotations personnalisées
- Migrer vers un framework (Spring, CDI) après avoir compris les bases

## 📚 Ressources et références

- [Martin Fowler - Inversion of Control Containers and the Dependency Injection pattern](https://martinfowler.com/articles/injection.html)
- Documentation Java - Interfaces et Polymorphisme

## 🔄 Prochaines étapes

Après avoir maîtrisé ces concepts de base, les prochaines étapes pourraient être :

1. Étudier les frameworks d'injection de dépendances (Spring, CDI)
2. Comprendre les différents scopes et cycles de vie des beans
3. Apprendre l'utilisation des annotations
4. Implémenter un conteneur IoC personnalisé

## 👤 Auteur
### Mody BALDE 
#### Developpeur fulstack Java Springboot/Angular


