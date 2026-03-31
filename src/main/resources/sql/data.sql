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
ON CONFLICT DO NOTHING;

-- ========================
-- 2. Insertion des utilisateurs
-- ========================
INSERT INTO users (user_name, email, phone, password, role, last_activity_at) VALUES
('admin_cesizen', 'admin@cesizen.fr', '', '$2a$10$zFJxkB6/4q3GxVJ1jXyqIu7v6x6mR8B7nQJ7mYc5Y0GhD8bK1r1Ge', 'ROLE_ADMIN', CURRENT_TIMESTAMP),
('test_user', 'user@cesizen.fr', '', '$2a$10$8K1p/a0dL1LXMIgoEDFrw.0v.4x.E.y.z.A.B.C.D.E.F.G.H.I.J', 'ROLE_USER', CURRENT_TIMESTAMP),
('jane_doe', 'jane@cesizen.fr', '', '$2a$10$E1kO31.H.G.y8.19g12.1.2.3.4.5.6.7.8.9.0.1.2.3.4.5.6.7.8', 'ROLE_USER', CURRENT_TIMESTAMP)
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
('Anxiété étudiante', 'Gérer le stress lié aux études', 'Techniques de relaxation et respiration', 1),
('Respiration', 'Améliorer la respiration pour réduire le stress', '10 techniques de respiration quotidienne', 1),
('Sommeil', 'Importance des cycles de sommeil', 'Bien dormir, c’est diminuer le niveau de fatigue et de stress, renforcer la mémoire et la concentration, améliorer l’humeur. C’est aussi mieux contrôler l’appétit et le poids. Dormir moins de 6 heures est associé à un risque plus élevé d’obésité, de diabète de type 2, d’hypertension, de pathologies cardiovasculaires et d’accidents. Le sommeil représente environ un tiers de notre vie. Il contribue à la régulation de la respiration, la circulation sanguine, la production d’hormones et la défense contre les bactéries et les virus. Ce n’est pas du temps perdu, c’est de la santé gagnée ! Dormir est vital pour le corps humain. A contrario, les troubles du sommeil tels que l’insomnie ou l’apnée du sommeil touchent de nombreuses personnes et peuvent avoir des conséquences graves sur notre santé.', 1),
('Nutrition', 'Manger équilibré', 'Conseils pour bien s''alimenter pendant les examens', 1)
ON CONFLICT DO NOTHING;

-- ========================
-- 5. Insertion du quiz 4 (Holmes & Rahe)
-- ========================
INSERT INTO quiz (title, description) VALUES
('Auto-diagnostic de stress', 'Auto-diagnostic du stress afin d''évaluer votre niveau de stress')
ON CONFLICT DO NOTHING;

-- ========================
-- 6. Questions du quiz 4 (Holmes & Rahe)
-- ========================
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Avez-vous été confronté à la mort d’un conjoint ou partenaire ces 12 derniers mois ?', 100, null, 1),
('Avez-vous divorcé ces 12 derniers mois ?', 73, null, 1),
('Avez-vous été séparé d’un conjoint ces 12 derniers mois ?', 65, null, 1),
('Avez-vous été emprisonné ces 12 derniers mois ?', 63, null, 1),
('Avez-vous subi la mort d’un membre proche de la famille ces 12 derniers mois ?', 63, null, 1),
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
ON CONFLICT DO NOTHING;

-- ========================
-- 7. Configuration des messages de résultat pour le quiz (Holmes & Rahe)
-- ========================
INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(1, 0, 150, 'Faible', 'Votre niveau de stress est faible selon l’échelle Holmes & Rahe.'),
(1, 151, 299, 'Modéré', 'Votre niveau de stress est modéré, faites attention et prenez des mesures pour gérer votre stress.'),
(1, 300, 1000, 'Élevé', 'Votre niveau de stress est élevé. Il est conseillé de consulter un professionnel ou d’adopter des stratégies de gestion du stress.')
ON CONFLICT DO NOTHING;