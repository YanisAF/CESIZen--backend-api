-- =========================================================
-- Script d'initialisation complet pour CESIZen
-- Compatible avec les entités User, Quiz, Question, ResultMessageConfig, Category, Page, ContentPage
-- =========================================================

-- ========================
-- 1. Insertion des catégories
-- ========================
INSERT INTO category (name) VALUES
('Bien-être mental'),
('Gestion du stress'),
('Sommeil et récupération'),
('Nutrition'),
('Activité physique'),
('Organisation et productivité'),
('Relations sociales'),
('Santé numérique')
ON CONFLICT (name) DO NOTHING;

-- ========================
-- 2. Insertion des utilisateurs
-- ========================
INSERT INTO users (user_name, email, phone, password, role, last_activity_at, is_active) VALUES
('admin_cesizen', 'admin@cesizen.fr', '0600708090', '$2a$10$zFJxkB6/4q3GxVJ1jXyqIu7v6x6mR8B7nQJ7mYc5Y0GhD8bK1r1Ge', 'ROLE_ADMIN', CURRENT_TIMESTAMP, true),
('test_user', 'user@cesizen.fr', '', '$2a$10$8K1p/a0dL1LXMIgoEDFrw.0v.4x.E.y.z.A.B.C.D.E.F.G.H.I.J', 'ROLE_USER', CURRENT_TIMESTAMP, true),
('jane_doe', 'jane@cesizen.fr', '', '$2a$10$E1kO31.H.G.y8.19g12.1.2.3.4.5.6.7.8.9.0.1.2.3.4.5.6.7.8', 'ROLE_USER', CURRENT_TIMESTAMP, true)
ON CONFLICT DO NOTHING;

-- ========================
-- 3. Insertion des pages liées aux catégories
-- ========================
INSERT INTO page (title, category_id, image_url) VALUES
('Comprendre l''anxiété étudiante', 1, 'https://example.com/images/anxiete.jpg')
ON CONFLICT DO NOTHING;

-- ========================
-- 4. Insertion du contenu des pages
-- ========================
INSERT INTO content (name, description, item, page_id) VALUES
('Anxiete etudiante',
 'Comprendre et surmonter l''anxiete liee aux etudes',
 'L anxiete etudiante est une reaction frequente face aux exigences academiques, aux examens et aux attentes personnelles. Elle peut se manifester par du stress intense, des troubles du sommeil, des difficultes de concentration ou encore une fatigue constante. Pour mieux la gerer : - Identifier les sources de stress (examens, charge de travail, pression sociale) - Adopter des techniques de respiration et de relaxation - Organiser son emploi du temps avec des pauses regulieres - Parler de ses difficultes avec des proches ou des professionnels. Se rappeler que l anxiete est normale permet deja de reduire son impact. L objectif n est pas de l eliminer, mais de la rendre gerable.',
 1),
('Respiration',
 'Apprendre a respirer pour mieux gerer son stress',
 'La respiration est un outil puissant pour calmer le systeme nerveux. Une respiration controlee permet de reduire immediatement le stress et d ameliorer la concentration. Techniques recommandees : - Respiration abdominale : inspirer lentement par le nez en gonflant le ventre, expirer par la bouche - Coherence cardiaque : 6 respirations par minute pendant 5 minutes - Respiration 4-7-8 : inspirer 4 secondes, retenir 7 secondes, expirer 8 secondes. Pratiquees quotidiennement, ces techniques ameliorent la gestion emotionnelle et reduisent l anxiete sur le long terme.',
 1),
('Sommeil',
 'Optimiser son sommeil pour ameliorer sa sante mentale',
 'Le sommeil est essentiel pour la recuperation physique et mentale. Un bon sommeil ameliore la memoire, la concentration et la gestion des emotions. Conseils pour mieux dormir : - Maintenir des horaires reguliers - Eviter les ecrans avant de dormir - Creer un environnement calme et sombre - Limiter la cafeine en fin de journee. Un manque de sommeil augmente le stress, diminue les performances et impacte negativement la sante globale. Dormir suffisamment est une priorite.',
 1),
('Nutrition',
 'Alimentation et performance mentale',
 'Une alimentation equilibree joue un role cle dans la gestion du stress et des performances intellectuelles. Bonnes pratiques : - Privilegier les aliments riches en omega 3 (poissons, noix) - Consommer des fruits et legumes pour les vitamines - Eviter les exces de sucre - Boire suffisamment d eau. Pendant les periodes d examen, manger sainement aide a maintenir une energie stable et une meilleure concentration.',
 1),
('Organisation',
 'Ameliorer sa productivite sans s epuiser',
 'Une bonne organisation permet de reduire considerablement le stress lie aux etudes. Methodes efficaces : - Technique Pomodoro (25 min de travail, 5 min de pause) - Priorisation des taches - Planification hebdomadaire. Travailler efficacement ne signifie pas travailler plus, mais travailler mieux.',
 1)
ON CONFLICT DO NOTHING;

-- ========================
-- 5. Insertion du quiz 1 (Holmes & Rahe)
-- ========================
INSERT INTO quiz (title, description) VALUES
('Auto-diagnostic de stress', 'Auto-diagnostic du stress afin d''évaluer votre niveau de stress')
ON CONFLICT (title) DO NOTHING;

-- ========================
-- 6. Questions du quiz 1 (Holmes & Rahe)
-- ========================
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Avez-vous été confronté à la mort d''un conjoint ou partenaire ces 12 derniers mois ?', 100, null, 1),
('Avez-vous divorcé ces 12 derniers mois ?', 73, null, 1),
('Avez-vous été séparé d''un conjoint ces 12 derniers mois ?', 65, null, 1),
('Avez-vous été emprisonné ces 12 derniers mois ?', 63, null, 1),
('Avez-vous subi la mort d''un membre proche de la famille ces 12 derniers mois ?', 63, null, 1),
('Avez-vous subi une blessure ou une maladie grave ces 12 derniers mois ?', 53, null, 1),
('Avez-vous été marié ces 12 derniers mois ?', 50, null, 1),
('Avez-vous perdu votre emploi ces 12 derniers mois ?', 47, null, 1),
('Avez-vous eu une réconciliation avec votre conjoint ces 12 derniers mois ?', 45, null, 1),
('Avez-vous pris votre retraite ces 12 derniers mois ?', 45, null, 1),
('Avez-vous eu un changement majeur dans vos finances ces 12 derniers mois ?', 38, null, 1),
('Avez-vous été confronté à un changement important dans vos conditions de travail ces 12 derniers mois ?', 39, null, 1),
('Avez-vous été enceinte ou avez-vous eu un enfant ces 12 derniers mois ?', 40, null, 1),
('Avez-vous commencé ou arrêté l''école ou l''université ces 12 derniers mois ?', 26, null, 1),
('Avez-vous changé de résidence principale ces 12 derniers mois ?', 20, null, 1)
ON CONFLICT (statement, quiz_id) DO NOTHING;

-- ========================
-- 7. Configuration des messages de résultat pour le quiz (Holmes & Rahe)
-- ========================
INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(1, 0, 150, 'FAIBLE', 'Votre niveau de stress est faible selon l''échelle Holmes & Rahe.'),
(1, 151, 299, 'MODERE', 'Votre niveau de stress est modéré, faites attention et prenez des mesures pour gérer votre stress.'),
(1, 300, 1000, 'ELEVE', 'Votre niveau de stress est élevé. Il est conseillé de consulter un professionnel ou d''adopter des stratégies de gestion du stress.')
ON CONFLICT DO NOTHING;

-- =========================================================
-- Script d'initialisation supplémentaire pour CESIZen
-- Nouveaux quiz (id 2, 3) et nouvelles pages (id 2, 3)
-- =========================================================

-- ========================
-- 1. Nouvelles pages liées aux catégories
-- ========================
INSERT INTO page (title, category_id, image_url) VALUES
('Gérer le stress en période d''examen', 2, 'https://example.com/images/stress-examen.jpg'),
('Optimiser son sommeil d''étudiant',    3, 'https://example.com/images/sommeil.jpg')
ON CONFLICT DO NOTHING;

-- ========================
-- 2. Contenu de la page 2 (Gérer le stress en période d'examen)
-- ========================
INSERT INTO content (name, description, item, page_id) VALUES
('Comprendre le stress d''examen',
 'Identifier les mécanismes du stress en période d''évaluation',
 'Le stress d examen est une forme de stress aigu qui se manifeste generalement dans les jours et semaines precedant une evaluation importante. Il est lie a la peur de l echec, a la pression sociale et aux enjeux percus. Signes courants : palpitations, maux de tete, troubles digestifs, difficultes a se concentrer. Strategies pour y faire face : - Anticiper en commencant les revisions tot - Decouper le programme en petites sessions - Pratiquer des exercices de relaxation chaque jour - Dormir suffisamment, meme la veille - Eviter les revisions de derniere minute qui amplifient l anxiete. Accepter une certaine dose de stress est sain : il peut stimuler la performance quand il reste sous controle.',
 2),

('Planification des révisions',
 'Organiser ses révisions pour réduire l''incertitude',
 'L incertitude est l une des principales sources du stress d examen. Une planification rigoureuse permet de la reduire considerablement. Methode conseillée : - Etablir la liste des chapitres a reviser par matiere - Creer un planning hebdomadaire realiste avec des blocs de 45 à 60 minutes - Prevoir des revisions espacees plutot que massives (la loi de la memoire a long terme) - Integrer des pauses regeneratrices et des activites plaisantes. Un planning visible et affiche reduit la charge mentale et donne un sentiment de maitrise sur la situation.',
 2),

('Techniques de gestion rapide du stress',
 'Des outils immédiats pour calmer le stress en situation d''examen',
 'Avant ou pendant un examen, ces techniques permettent de retrouver rapidement son calme : - La respiration carree : inspirer 4 sec, retenir 4 sec, expirer 4 sec, pause 4 sec - La methode 5-4-3-2-1 : nommer 5 choses vues, 4 entendues, 3 touchees, 2 sentees, 1 goutee - La visualisation positive : imaginer un examen reussi en detail - Le balayage corporel : prendre conscience de chaque partie du corps pour relacher les tensions. Practiquees regulierement avant les examens, ces techniques deviennent des reflexes efficaces en situation de pression.',
 2),

('Alimentation et stress',
 'Ce que l''on mange influence la gestion du stress',
 'Pendant les periodes de revision et d examens, l alimentation joue un role direct sur le niveau d energie et la regulation emotionnelle. A privilegier : fruits, legumes, cereales completes, proteines maigres, oleagineux (noix, amandes). A eviter ou limiter : cafeine en exces (augmente l anxiete), sucres rapides (pics glycemiques suivis de baisses d energie), alcool (perturbe le sommeil et la cognition). S hydrater regulierement ameliore la concentration et reduit les maux de tete lies au travail intensif.',
 2)

ON CONFLICT DO NOTHING;

-- ========================
-- 3. Contenu de la page 3 (Optimiser son sommeil d'étudiant)
-- ========================
INSERT INTO content (name, description, item, page_id) VALUES
('Comprendre les cycles du sommeil',
 'Connaître les bases pour mieux récupérer',
 'Le sommeil est organise en cycles de 90 minutes environ, alternant sommeil leger, sommeil profond et sommeil paradoxal. Chaque phase a un role : - Sommeil profond : recuperation physique, consolidation de la memoire declarative - Sommeil paradoxal : traitement emotionnel, memorisation des procedures. Pour un etudiant, dormir 7 a 9 heures permet de consolider les apprentissages de la journee. Se lever en milieu de cycle provoque une fatigue intense. Utiliser une alarme progressive ou calculer ses heures de sommeil en multiples de 90 min aide a mieux se reveiller.',
 3),

('Hygiène du sommeil',
 'Les habitudes qui favorisent un sommeil réparateur',
 'L hygiene du sommeil regroupe l ensemble des comportements qui favorisent un endormissement rapide et un sommeil de qualite. Recommandations : - Se coucher et se lever a heures fixes, meme le week-end - Eviter les ecrans au moins 30 minutes avant de dormir (la lumiere bleue supprime la melatonine) - Creer un environnement propice : chambre fraiche (18-20 C), sombre, calme - Eviter la cafeine apres 14h - Ne pas manger lourd le soir - Pratiquer une activite relaxante en fin de soiree (lecture, etirements, meditation). Une bonne hygiene du sommeil est la base d une sante mentale equilibree.',
 3),

('Gérer les nuits courtes',
 'Stratégies pour les périodes de surcharge',
 'Il arrive que les contraintes academiques ou professionnelles imposent des nuits courtes. Dans ces situations : - Privilegier une sieste courte (10 a 20 minutes) dans l apres-midi pour recuperer sans perturber le sommeil nocturne - Ne pas compenser le week-end en dormant excessivement : cela decale le rythme circadien - Maintenir autant que possible l heure de reveil fixe - Reduire les activites non essentielles plutot que le sommeil. Le manque de sommeil chronique impacte la memoire, la prise de decision et la resistance au stress. Il ne faut pas le banaliser.',
 3),

('Numérique et sommeil',
 'L''impact des écrans sur la qualité du sommeil',
 'L utilisation des smartphones, tablettes et ordinateurs le soir est l une des principales causes de degradation du sommeil chez les jeunes. La lumiere bleue emise par les ecrans supprime la secretion de melatonine, retardant l endormissement. Les contenus stimulants (reseaux sociaux, videos, jeux) maintiennent le cerveau en etat d eveil. Solutions pratiques : - Activer le mode nuit ou utiliser des lunettes anti-lumiere bleue - Poser le telephone hors de la chambre ou le mettre en mode avion - Remplacer les ecrans par une activite calme (journal, lecture papier, exercices de respiration) - Definir une heure limite d utilisation des ecrans chaque soir.',
 3)

ON CONFLICT DO NOTHING;

-- ========================
-- 4. Quiz 2 : Échelle de bien-être de Warwick-Edinburgh (WEMWBS)
-- ========================
INSERT INTO quiz (title, description) VALUES
('Évaluation du bien-être mental',
 'Questionnaire de bien-être mental de Warwick-Edinburgh pour mesurer votre état de bien-être général sur les deux dernières semaines')
ON CONFLICT (title) DO NOTHING;

-- ========================
-- 5. Questions du quiz 2 (WEMWBS - style Likert 1 à 5)
-- Chaque question : score_value représente le poids unitaire (1),
-- la valeur Likert sera multipliée côté applicatif.
-- ========================
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Au cours des deux dernières semaines, je me suis senti(e) optimiste quant à l''avenir.', 1, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) utile.', 1, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) détendu(e).', 1, null, 2),
('Au cours des deux dernières semaines, je me suis intéressé(e) aux autres personnes.', 1, null, 2),
('Au cours des deux dernières semaines, j''ai eu de l''énergie à revendre.', 1, null, 2),
('Au cours des deux dernières semaines, j''ai su faire face à mes problèmes.', 1, null, 2),
('Au cours des deux dernières semaines, j''ai pu penser de façon claire.', 1, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) bien dans ma peau.', 1, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) proche des autres.', 1, null, 2),
('Au cours des deux dernières semaines, j''ai eu confiance en moi.', 1, null, 2),
('Au cours des deux dernières semaines, j''ai été capable de me décider.', 1, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) aimé(e) et accepté(e).', 1, null, 2),
('Au cours des deux dernières semaines, j''ai été intéressé(e) par des choses nouvelles.', 1, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) plein(e) d''entrain.', 1, null, 2)
ON CONFLICT (statement, quiz_id) DO NOTHING;

-- ========================
-- 6. Messages de résultat pour le quiz 2 (WEMWBS)
-- Score total = somme des valeurs Likert (1-5) × score_value (1) pour 14 items
-- Min théorique : 14 | Max théorique : 70
-- ========================
INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(2, 14, 40, 'FAIBLE',
 'Votre score indique un bien-être mental faible. Il serait bénéfique d''en parler à un professionnel de santé ou à un conseiller et d''explorer des pratiques de soin de soi adaptées.'),
(2, 41, 59, 'MODERE',
 'Votre bien-être mental est dans la moyenne. Vous disposez de ressources mais il existe des axes d''amélioration : activité physique, lien social, sommeil et gestion du stress peuvent encore être renforcés.'),
(2, 60, 70, 'ELEVE',
 'Votre bien-être mental est élevé. Continuez à entretenir les habitudes positives qui vous permettent de vous épanouir.')
ON CONFLICT DO NOTHING;

-- ========================
-- 7. Quiz 3 : Inventaire d'anxiété de Beck (BAI) simplifié
-- ========================
INSERT INTO quiz (title, description) VALUES
('Inventaire d''anxiété',
 'Version simplifiée de l''inventaire d''anxiété de Beck pour évaluer l''intensité de vos symptômes anxieux au cours de la dernière semaine')
ON CONFLICT (title) DO NOTHING;

-- ========================
-- 8. Questions du quiz 3 (BAI simplifié - score 0 à 3 par item)
-- score_value = poids unitaire (1), valeur réelle calculée côté applicatif
-- ========================
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Au cours de la dernière semaine, avez-vous ressenti des engourdissements ou des fourmillements ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu des bouffées de chaleur ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu les jambes molles ou tremblantes ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous été incapable de vous détendre ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur que le pire arrive ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu des étourdissements ou la tête légère ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu des palpitations cardiaques ?', 1, null, 3),
('Au cours de la dernière semaine, vous êtes-vous senti(e) instable ou peu sûr(e) de vous ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous été terrorisé(e) ou effrayé(e) ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous été nerveux/nerveuse ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu une sensation d''étouffement ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu des tremblements des mains ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu des tremblements dans tout le corps ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur de perdre le contrôle de vous-même ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu du mal à respirer ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur de mourir ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur ou avez-vous été anxieux/anxieuse ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu des indigestions ou un inconfort abdominal ?', 1, null, 3),
('Au cours de la dernière semaine, vous êtes-vous senti(e) sur le point de défaillir ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous eu le visage rouge ou chaud ?', 1, null, 3),
('Au cours de la dernière semaine, avez-vous transpiré(e) (sans chaleur) ?', 1, null, 3)
ON CONFLICT (statement, quiz_id) DO NOTHING;

-- ========================
-- 9. Messages de résultat pour le quiz 3 (BAI simplifié)
-- Score total = somme des valeurs (0-3) × score_value (1) pour 21 items
-- Min théorique : 0 | Max théorique : 63
-- Seuils cliniques BAI standard
-- ========================
INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(3, 0,  21, 'FAIBLE',
 'Votre niveau d''anxiété est faible. Quelques symptômes peuvent être présents mais ils n''interfèrent pas significativement avec votre quotidien.'),
(3, 22, 35, 'MODERE',
 'Votre niveau d''anxiété est modéré. Il serait utile d''explorer des techniques de gestion du stress et, si les symptômes persistent, d''en discuter avec un professionnel de santé.'),
(3, 36, 63, 'ELEVE',
 'Votre niveau d''anxiété est élevé. Il est fortement recommandé de consulter un médecin ou un psychologue afin d''obtenir un soutien adapté.')
ON CONFLICT DO NOTHING;