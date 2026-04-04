-- =========================================================
-- Script d'initialisation pour CESIZen
-- =========================================================

-- 1. Insertion des catégories
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

-- 2. Insertion des pages (1 à 14)
INSERT INTO page (title, category_id, image_url) VALUES
('Comprendre l''anxiété étudiante',         1, 'https://example.com/images/anxiete.jpg'),
('Gérer le stress en période d''examen',    2, 'https://example.com/images/stress-examen.jpg'),
('Optimiser son sommeil d''étudiant',       3, 'https://example.com/images/sommeil.jpg'),
('Mieux manger pour mieux étudier',         4, 'https://example.com/images/nutrition.jpg'),
('Bouger pour aller mieux',                 5, 'https://example.com/images/activite.jpg'),
('Organisation et gestion du temps',        6, 'https://example.com/images/organisation.jpg'),
('Entretenir ses relations sociales',       7, 'https://example.com/images/relations.jpg'),
('Apprivoiser les écrans',                  8, 'https://example.com/images/numerique.jpg'),
('Faire face aux pensées négatives',        1, 'https://example.com/images/pensees.jpg'),
('Méditation et pleine conscience',         2, 'https://example.com/images/meditation.jpg'),
('Gérer la charge mentale',                 3, 'https://example.com/images/charge-mentale.jpg'),
('Prendre soin de sa santé physique',       4, 'https://example.com/images/sante-physique.jpg'),
('Réseaux sociaux et bien-être',            8, 'https://example.com/images/reseaux-sociaux.jpg')
ON CONFLICT DO NOTHING;

-- 3. Insertion des contenus des pages (1 à 14)
INSERT INTO content (name, description, item, page_id) VALUES
('Anxiete etudiante', 'Comprendre et surmonter l''anxiete liee aux etudes', 'L anxiete etudiante est une reaction frequente face aux exigences academiques, aux examens et aux attentes personnelles. Elle peut se manifester par du stress intense, des troubles du sommeil, des difficultes de concentration ou encore une fatigue constante. Pour mieux la gerer : - Identifier les sources de stress (examens, charge de travail, pression sociale) - Adopter des techniques de respiration et de relaxation - Organiser son emploi du temps avec des pauses regulieres - Parler de ses difficultes avec des proches ou des professionnels. Se rappeler que l anxiete est normale permet deja de reduire son impact. L objectif n est pas de l eliminer, mais de la rendre gerable.', 1),
('Respiration', 'Apprendre a respirer pour mieux gerer son stress', 'La respiration est un outil puissant pour calmer le systeme nerveux. Une respiration controlee permet de reduire immediatement le stress et d ameliorer la concentration. Techniques recommandees : - Respiration abdominale : inspirer lentement par le nez en gonflant le ventre, expirer par la bouche - Coherence cardiaque : 6 respirations par minute pendant 5 minutes - Respiration 4-7-8 : inspirer 4 secondes, retenir 7 secondes, expirer 8 secondes. Pratiquees quotidiennement, ces techniques ameliorent la gestion emotionnelle et reduisent l anxiete sur le long terme.', 1),
('Sommeil', 'Optimiser son sommeil pour ameliorer sa sante mentale', 'Le sommeil est essentiel pour la recuperation physique et mentale. Un bon sommeil ameliore la memoire, la concentration et la gestion des emotions. Conseils pour mieux dormir : - Maintenir des horaires reguliers - Eviter les ecrans avant de dormir - Creer un environnement calme et sombre - Limiter la cafeine en fin de journee. Un manque de sommeil augmente le stress, diminue les performances et impacte negativement la sante globale. Dormir suffisamment est une priorite.', 1),
('Nutrition', 'Alimentation et performance mentale', 'Une alimentation equilibree joue un role cle dans la gestion du stress et des performances intellectuelles. Bonnes pratiques : - Privilegier les aliments riches en omega 3 (poissons, noix) - Consommer des fruits et legumes pour les vitamines - Eviter les exces de sucre - Boire suffisamment d eau. Pendant les periodes d examen, manger sainement aide a maintenir une energie stable et une meilleure concentration.', 1),
('Organisation', 'Ameliorer sa productivite sans s epuiser', 'Une bonne organisation permet de reduire considerablement le stress lie aux etudes. Methodes efficaces : - Technique Pomodoro (25 min de travail, 5 min de pause) - Priorisation des taches - Planification hebdomadaire. Travailler efficacement ne signifie pas travailler plus, mais travailler mieux.', 1),
('Activite physique', 'Bouger pour reduire le stress', 'L activite physique est un excellent moyen de reduire le stress et d ameliorer le bien etre mental. Bienfaits : - Liberation d endorphines - Reduction de l anxiete - Amelioration du sommeil. 20 a 30 minutes d activite par jour peuvent avoir un impact significatif.', 1),
('Santé numérique', 'Limiter l impact du numérique sur le stress', 'L utilisation excessive des écrans peut augmenter le stress et perturber le sommeil. Conseils : - Limiter le temps sur les reseaux sociaux - Desactiver les notifications inutiles - Faire des pauses regulieres - Eviter les ecrans avant le coucher. Une meilleure gestion du numerique permet de retrouver du calme et de la concentration.', 1)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Comprendre le stress d''examen', 'Identifier les mécanismes du stress en période d''évaluation', 'Le stress d examen est une forme de stress aigu lie a la peur de l echec et aux enjeux percus. Signes courants : palpitations, maux de tete, troubles digestifs, difficultes de concentration. Strategies : anticiper les revisions, decouper le programme en petites sessions, pratiquer la relaxation, dormir suffisamment. Accepter une certaine dose de stress est sain : il stimule la performance quand il reste sous controle.', 2),
('Planification des révisions', 'Réduire l''incertitude par l''organisation', 'Un planning rigoureux reduit considerablement le stress d examen. Etablir la liste des chapitres par matiere, creer un planning hebdomadaire realiste avec des blocs de 45 a 60 minutes, prevoir des revisions espacees plutot que massives. Un planning visible reduit la charge mentale et donne un sentiment de maitrise.', 2),
('Techniques rapides anti-stress', 'Des outils immédiats pour retrouver son calme', 'Respiration carree : inspirer 4 sec, retenir 4 sec, expirer 4 sec, pause 4 sec. Methode 5-4-3-2-1 : nommer 5 choses vues, 4 entendues, 3 touchees. Visualisation positive : imaginer un examen reussi en detail. Balayage corporel : relacher les tensions muscle par muscle. Pratiquees regulierement, ces techniques deviennent des reflexes.', 2),
('Alimentation et stress', 'Ce que l''on mange influence la gestion du stress', 'Pendant les revisions, privilegier : fruits, legumes, cereales completes, proteines maigres, oleagineux. Eviter : cafeine en exces, sucres rapides, alcool. S hydrater regulierement ameliore la concentration et reduit les maux de tete.', 2)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Les cycles du sommeil', 'Connaître les bases pour mieux récupérer', 'Le sommeil est organise en cycles de 90 minutes alternant sommeil leger, profond et paradoxal. Le sommeil profond consolide la memoire declarative, le sommeil paradoxal traite les emotions. Dormir 7 a 9 heures consolide les apprentissages. Calculer ses heures de sommeil en multiples de 90 min aide a mieux se reveiller.', 3),
('Hygiène du sommeil', 'Les habitudes qui favorisent un sommeil réparateur', 'Se coucher et se lever a heures fixes, eviter les ecrans 30 min avant de dormir, chambre fraiche (18-20 C), sombre et calme, eviter la cafeine apres 14h, ne pas manger lourd le soir, pratiquer une activite relaxante en fin de soiree. Une bonne hygiene du sommeil est la base d une sante mentale equilibree.', 3),
('Gérer les nuits courtes', 'Stratégies pour les périodes de surcharge', 'En periode de surcharge, privilegier une sieste courte (10 a 20 min) l apres-midi. Ne pas compenser le week-end en dormant excessivement : cela decale le rythme circadien. Maintenir l heure de reveil fixe. Le manque de sommeil chronique impacte la memoire, la decision et la resistance au stress.', 3),
('Numérique et sommeil', 'L''impact des écrans sur la qualité du sommeil', 'La lumiere bleue supprime la secretion de melatonine et retarde l endormissement. Solutions : activer le mode nuit, poser le telephone hors de la chambre, remplacer les ecrans par une activite calme, définir une heure limite d utilisation des ecrans chaque soir.', 3)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Nutrition et cognition', 'Les liens entre alimentation et performances intellectuelles', 'Le cerveau consomme environ 20 % de l energie totale du corps. Une alimentation equilibree est essentielle pour maintenir la concentration, la memoire et la rapidite de traitement. Les omega 3 favorisent la plasticite neuronale. Le glucose issu de cereales completes fournit une energie stable sans pic glycemique.', 4),
('Les aliments a privilegier', 'Que mettre dans son assiette pour performer', 'A privilegier : poissons gras (saumon, maquereau, sardines), oeufs, legumineuses, fruits et legumes colores, cereales completes, oleagineux, chocolat noir. Ces aliments apportent vitamines B, magnesium, antioxydants et omega 3 indispensables au bon fonctionnement cerebral.', 4),
('Hydratation et concentration', 'L''eau, premier nutriment du cerveau', 'Une deshydratation de seulement 1 a 2 % suffit a reduire la concentration et augmenter la fatigue. Boire 1,5 a 2 litres d eau par jour. Limiter les boissons sucrees et energisantes qui provoquent des pics puis des chutes d energie.', 4),
('Rythme des repas', 'Quand manger pour rester efficace', 'Sauter le petit-dejeuner reduit les capacites de memorisation le matin. Privilegier trois repas equilibres et une collation saine si besoin. Eviter les repas trop copieux le midi qui provoquent une somnolence post-prandiale.', 4)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Activité physique et santé mentale', 'Les bienfaits prouvés du mouvement sur le bien-être', 'L activite physique reguliere reduit les symptomes d anxiete et de depression, ameliore la qualite du sommeil et renforce l estime de soi. Elle stimule la production d endorphines, de serotonine et de BDNF. 30 minutes d activite moderee par jour suffisent pour obtenir des benefices significatifs.', 5),
('Trouver une activité adaptée', 'Bouger selon ses envies et ses contraintes', 'Il n est pas necessaire de pratiquer un sport intense. La marche rapide, le velo, la natation, le yoga ou la danse sont tout aussi benefiques. L essentiel est la regularite. Choisir une activite plaisante augmente les chances de s y tenir sur le long terme.', 5),
('Intégrer le mouvement au quotidien', 'Bouger sans forcément faire du sport', 'Prendre les escaliers, marcher entre les cours, faire des etirements entre deux sessions de travail, se lever toutes les heures : ces micro-activites limitent les effets negatifs de la sedentarite. Meme 10 minutes de marche ameliorent l humeur et la concentration immediatement.', 5)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Pourquoi s''organiser réduit le stress', 'Le lien entre organisation et bien-être mental', 'Le sentiment de debordement est l une des principales sources de stress chez les etudiants. Avoir une vision claire de ses taches et de ses priorites reduit l anxiete liee a l incertitude. Une bonne organisation ne signifie pas tout controler, mais se donner les moyens d agir plutot que de subir.', 6),
('La méthode Pomodoro', 'Travailler par blocs pour rester concentré', 'La technique Pomodoro consiste a travailler 25 minutes de facon concentree, puis a faire une pause de 5 minutes. Apres 4 cycles, une pause longue de 15 a 30 minutes est accordee. Cette methode limite la procrastination, reduit la fatigue mentale et donne un rythme clair a la journee.', 6),
('Prioriser avec la matrice d''Eisenhower', 'Distinguer l''urgent de l''important', 'La matrice d Eisenhower classe les taches en quatre quadrants : urgent et important (faire maintenant), important mais pas urgent (planifier), urgent mais pas important (deleguer), ni urgent ni important (eliminer). Cet outil aide a se concentrer sur ce qui compte vraiment.', 6),
('Limiter les distractions', 'Reprendre le contrôle de son attention', 'Les notifications, les reseaux sociaux et les interruptions sont les principaux ennemis de la concentration. Solutions : desactiver les notifications pendant les sessions de travail, utiliser des applications de blocage de sites, travailler dans un environnement calme.', 6)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Le soutien social, facteur de résilience', 'Pourquoi les liens humains protègent la santé mentale', 'Le soutien social est l un des facteurs de protection les plus puissants contre le stress et la depression. Avoir des personnes a qui parler reduit significativement l impact des evenements stressants. L isolement amplifie les difficultes et augmente le risque de troubles mentaux.', 7),
('Communiquer efficacement', 'S''exprimer et écouter pour mieux se connecter', 'Une communication saine repose sur l expression claire de ses besoins et emotions, l ecoute active sans interrompre ni juger, la reformulation pour valider ce qu on a entendu. Eviter la communication passive-agressive ou les non-dits qui creent des malentendus.', 7),
('Gérer les conflits', 'Transformer les tensions en opportunités de dialogue', 'Les conflits font partie de toute relation. Choisir le bon moment pour en parler, exprimer ses ressentis sans accuser l autre (utiliser le je plutot que le tu), chercher une solution acceptable pour les deux parties. Un conflit bien gere renforce souvent la relation.', 7)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Le temps d''écran et la santé', 'Comprendre l''impact du numérique sur le corps et l''esprit', 'Un usage excessif des ecrans est associe a une augmentation de l anxiete, une degradation du sommeil, des troubles de la concentration et une diminution du bien-etre general. La stimulation permanente des contenus numeriques maintient le cerveau dans un etat d eveil chronique.', 8),
('Établir des règles numériques', 'Reprendre le contrôle de ses usages', 'Quelques regles simples pour un usage plus sain : définir des plages sans ecran (repas, premiere heure du matin, heure avant de dormir), desactiver les notifications non essentielles, regrouper la consultation des messages plutot que de repondre en temps reel.', 8),
('La déconnexion choisie', 'Les bénéfices d''une pause numérique', 'Prendre des pauses numeriques regulieres (une demi-journee par semaine sans ecran) permet de retrouver de la concentration, de la creativite et une meilleure qualite de presence. Ces pauses reduisent le FOMO et aident a renouer avec des activites a faible stimulation.', 8)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Comprendre les distorsions cognitives', 'Ces schémas de pensée qui amplifient la souffrance', 'Les distorsions cognitives sont des biais de pensee automatiques qui deforment la realite. Exemples : catastrophisation (imaginer le pire), pensee tout-ou-rien, personnalisation, filtre mental (ne retenir que le negatif). Les identifier est la premiere etape pour les questionner.', 9),
('La restructuration cognitive', 'Remettre en question ses pensées automatiques', 'Face a une pensee negative automatique, se poser : Quelle est la preuve que cette pensee est vraie ? Y a-t-il une autre facon d interpreter la situation ? Quelle serait ma reaction si un ami me disait la meme chose ? Ce travail permet de remplacer les pensees irrationnelles par des interpretations plus equilibrees.', 9),
('Journal de gratitude', 'Réorienter l''attention vers le positif', 'Tenir un journal de gratitude consiste a noter chaque soir trois choses positives de la journee, meme minimes. Cette pratique entraine progressivement le cerveau a detecter et retenir les aspects positifs de l experience, contrebalancant le biais de negativite naturel.', 9)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Qu''est-ce que la pleine conscience', 'Définition et principes de la mindfulness', 'La pleine conscience est la capacite a porter son attention sur le moment present, de facon intentionnelle et sans jugement. La pratique reguliere reduit le stress, l anxiete, la rumination et ameliore la regulation emotionnelle. Elle a ete adaptee en protocoles therapeutiques valides scientifiquement.', 10),
('Commencer la méditation', 'Premiers pas accessibles pour tout le monde', 'Commencer par 5 a 10 minutes par jour : s asseoir confortablement, fermer les yeux, porter l attention sur la respiration, observer les pensees sans s y accrocher comme des nuages qui passent. Des applications comme Petit Bambou ou Headspace proposent des guides pour debutants.', 10),
('Méditation en mouvement', 'Intégrer la pleine conscience dans les activités quotidiennes', 'Marcher en pleine conscience, manger lentement en savourant chaque bouchee, ecouter de la musique avec toute son attention : ces pratiques cultivent la presence au moment actuel. Ces micro-moments de pleine conscience ont un effet cumulatif sur le bien-etre.', 10)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Qu''est-ce que la charge mentale', 'Comprendre ce poids invisible qui épuise', 'La charge mentale designe l ensemble des taches cognitives liees a la planification et l organisation des responsabilites quotidiennes. Elle se manifeste par une sensation de cerveau constamment en activite, une difficulte a se deconnecter, une fatigue qui ne disparait pas avec le repos.', 11),
('Externaliser et déléguer', 'Alléger le poids en partageant les responsabilités', 'Pour reduire la charge mentale : noter les taches dans un systeme externe plutot que de tout garder en tete, communiquer clairement les responsabilites avec son entourage, accepter de deleguer meme imparfaitement. Tout garder pour soi par perfectionnisme est une source majeure d epuisement.', 11),
('Créer des routines', 'Automatiser pour libérer de l''espace mental', 'Les routines transforment des decisions repetitives en automatismes. Routine du matin (meme ordre de preparation), routine du soir (preparation du lendemain), routine de travail (meme heure de debut). Moins de decisions signifie moins de fatigue mentale.', 11)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('Santé physique et santé mentale', 'Un duo indissociable', 'La sante physique et la sante mentale sont profondement interconnectees. Un manque de sommeil ou une mauvaise alimentation ont des repercussions directes sur l humeur et la resilience. A l inverse, un etat psychologique degrade peut affaiblir le systeme immunitaire.', 12),
('Écouter les signaux du corps', 'Reconnaître et respecter ses limites', 'La fatigue, les tensions musculaires, les maux de tete ou les troubles digestifs sont souvent des signaux d un desequilibre. Apprendre a les reconnaitre plutot qu a les ignorer permet de mieux doser son energie et d eviter le burnout.', 12),
('Prévention et suivi médical', 'Ne pas négliger les rendez-vous de santé', 'Un suivi medical regulier permet de detecter precocement d eventuels problemes. Beaucoup d etudiants negligent leur sante faute de temps ou de budget. Les centres de sante universitaires proposent souvent des consultations gratuites ou a tarif reduit.', 12)
ON CONFLICT DO NOTHING;

INSERT INTO content (name, description, item, page_id) VALUES
('L''impact des réseaux sociaux sur l''estime de soi', 'Comparaison sociale à l''ère numérique', 'Les reseaux sociaux favorisent la comparaison sociale ascendante : on se compare a des personnes qui semblent plus heureuses ou plus reussies. Cette comparaison constante erode l estime de soi et augmente l anxiete sociale. Les contenus partages sont soigneusement selectionnes et ne refletent pas la realite.', 13),
('Usage passif vs usage actif', 'Comment on utilise les réseaux fait toute la différence', 'L usage passif (scroller sans interagir) est associe a une augmentation des emotions negatives. L usage actif (commenter, creer du contenu, maintenir des liens reels) a des effets neutres voire positifs. Privilegier les interactions authentiques et les communautes qui apportent de la valeur.', 13),
('Faire le tri dans ses abonnements', 'Curating son environnement numérique', 'Se desabonner des comptes qui generent de la jalousie ou de l insatisfaction. Suivre des comptes qui inspirent, informent ou divertissent positivement. Faire le point regulierement sur la facon dont on se sent apres avoir utilise un reseau social.', 13)
ON CONFLICT DO NOTHING;

-- 4. Insertion des quiz (1 à 13)
INSERT INTO quiz (title, description) VALUES
('Auto-diagnostic de stress', 'Auto-diagnostic du stress afin d''évaluer votre niveau de stress'),
('Évaluation du bien-être mental', 'Questionnaire inspiré de l''échelle WEMWBS pour évaluer votre bien-être général sur les deux dernières semaines'),
('Inventaire d''anxiété', 'Questionnaire inspiré de l''inventaire d''anxiété de Beck pour évaluer l''intensité de vos symptômes anxieux au cours de la dernière semaine'),
('Qualité du sommeil', 'Questionnaire inspiré de l''index de qualité du sommeil de Pittsburgh (PSQI) pour évaluer la qualité de votre sommeil sur le dernier mois'),
('Échelle de stress perçu', 'Questionnaire inspiré de l''échelle de stress perçu (PSS-10) de Cohen pour évaluer votre niveau de stress ressenti au cours du dernier mois'),
('Évaluation de l''épuisement étudiant', 'Burnout de Maslach (MBI-SS) adapté aux étudiants pour évaluer votre niveau de fatigue liée aux études'),
('Évaluation des habitudes alimentaires', 'Questionnaire pour évaluer la qualité de vos habitudes alimentaires et leur impact sur votre bien-être'),
('Niveau d''activité physique', 'Questionnaire pour évaluer votre niveau d''activité physique hebdomadaire et ses effets sur votre santé'),
('Évaluation du lien social', 'Questionnaire pour évaluer la qualité et la solidité de votre réseau de soutien social'),
('Évaluation de la procrastination', 'Questionnaire pour mesurer votre tendance à remettre à plus tard les tâches importantes'),
('Usage du numérique', 'Questionnaire pour évaluer si votre rapport aux écrans et aux réseaux sociaux est équilibré ou potentiellement problématique'),
('Estime de soi', 'Questionnaire inspiré de l''échelle d''estime de soi de Rosenberg pour évaluer l''image que vous avez de vous-même'),
('Gestion des émotions', 'Questionnaire pour évaluer votre capacité à identifier, comprendre et réguler vos émotions au quotidien')
ON CONFLICT (title) DO NOTHING;

-- 5. Questions et Messages de résultat
-- Quiz 1
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

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(1, 0, 150, 'FAIBLE', 'Votre niveau de stress est faible selon l''échelle Holmes & Rahe.'),
(1, 151, 299, 'MODERE', 'Votre niveau de stress est modéré, faites attention et prenez des mesures pour gérer votre stress.'),
(1, 300, 1000, 'ELEVE', 'Votre niveau de stress est élevé. Il est conseillé de consulter un professionnel ou d''adopter des stratégies de gestion du stress.')
ON CONFLICT DO NOTHING;

-- Quiz 2
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Au cours des deux dernières semaines, je me suis senti(e) optimiste quant à l''avenir.', 70, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) utile.', 65, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) détendu(e).', 80, null, 2),
('Au cours des deux dernières semaines, je me suis intéressé(e) aux autres personnes.', 60, null, 2),
('Au cours des deux dernières semaines, j''ai eu de l''énergie à revendre.', 75, null, 2),
('Au cours des deux dernières semaines, j''ai su faire face à mes problèmes.', 85, null, 2),
('Au cours des deux dernières semaines, j''ai pu penser de façon claire.', 80, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) bien dans ma peau.', 90, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) proche des autres.', 65, null, 2),
('Au cours des deux dernières semaines, j''ai eu confiance en moi.', 85, null, 2),
('Au cours des deux dernières semaines, j''ai été capable de me décider.', 70, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) aimé(e) et accepté(e).', 90, null, 2),
('Au cours des deux dernières semaines, j''ai été intéressé(e) par des choses nouvelles.', 60, null, 2),
('Au cours des deux dernières semaines, je me suis senti(e) plein(e) d''entrain.', 75, null, 2)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(2, 0, 400, 'FAIBLE', 'Votre bien-être mental semble fragile en ce moment. Il serait bénéfique d''en parler à un professionnel ou à une personne de confiance.'),
(2, 401, 700, 'MODERE', 'Votre bien-être mental est dans la moyenne. Des axes d''amélioration existent : activité physique, lien social et gestion du stress peuvent encore être renforcés.'),
(2, 701, 1260, 'ELEVE', 'Votre bien-être mental est élevé. Continuez à entretenir les habitudes positives qui vous permettent de vous épanouir.')
ON CONFLICT DO NOTHING;

-- Quiz 3
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Au cours de la dernière semaine, avez-vous ressenti des engourdissements ou des fourmillements ?', 45, null, 3),
('Au cours de la dernière semaine, avez-vous eu des bouffées de chaleur ?', 50, null, 3),
('Au cours de la dernière semaine, avez-vous eu les jambes molles ou tremblantes ?', 55, null, 3),
('Au cours de la dernière semaine, avez-vous été incapable de vous détendre ?', 70, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur que le pire arrive ?', 80, null, 3),
('Au cours de la dernière semaine, avez-vous eu des étourdissements ou la tête légère ?', 60, null, 3),
('Au cours de la dernière semaine, avez-vous eu des palpitations cardiaques ?', 65, null, 3),
('Au cours de la dernière semaine, vous êtes-vous senti(e) instable ou peu sûr(e) de vous ?', 70, null, 3),
('Au cours de la dernière semaine, avez-vous été terrorisé(e) ou effrayé(e) ?', 90, null, 3),
('Au cours de la dernière semaine, avez-vous été nerveux/nerveuse ?', 55, null, 3),
('Au cours de la dernière semaine, avez-vous eu une sensation d''étouffement ?', 75, null, 3),
('Au cours de la dernière semaine, avez-vous eu des tremblements des mains ?', 50, null, 3),
('Au cours de la dernière semaine, avez-vous eu des tremblements dans tout le corps ?', 60, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur de perdre le contrôle de vous-même ?', 85, null, 3),
('Au cours de la dernière semaine, avez-vous eu du mal à respirer ?', 70, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur de mourir ?', 100, null, 3),
('Au cours de la dernière semaine, avez-vous eu peur ou avez-vous été anxieux/anxieuse ?', 65, null, 3),
('Au cours de la dernière semaine, avez-vous eu des indigestions ou un inconfort abdominal ?', 45, null, 3),
('Au cours de la dernière semaine, vous êtes-vous senti(e) sur le point de défaillir ?', 80, null, 3),
('Au cours de la dernière semaine, avez-vous eu le visage rouge ou chaud ?', 40, null, 3),
('Au cours de la dernière semaine, avez-vous transpiré(e) sans raison de chaleur ?', 50, null, 3)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(3, 0, 400, 'FAIBLE', 'Votre niveau d''anxiété est faible. Quelques symptômes peuvent être présents mais ils n''interfèrent pas significativement avec votre quotidien.'),
(3, 401, 700, 'MODERE', 'Votre niveau d''anxiété est modéré. Explorer des techniques de gestion du stress et en parler à un professionnel peut vous aider.'),
(3, 701, 1450, 'ELEVE', 'Votre niveau d''anxiété est élevé. Il est fortement recommandé de consulter un médecin ou un psychologue afin d''obtenir un soutien adapté.')
ON CONFLICT DO NOTHING;

-- Quiz 4
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Au cours du dernier mois, avez-vous mis plus de 30 minutes à vous endormir ?', 70, null, 4),
('Au cours du dernier mois, vous êtes-vous réveillé(e) au milieu de la nuit ?', 65, null, 4),
('Au cours du dernier mois, avez-vous dû vous lever pour aller aux toilettes la nuit ?', 50, null, 4),
('Au cours du dernier mois, avez-vous eu du mal à respirer correctement durant le sommeil ?', 80, null, 4),
('Au cours du dernier mois, avez-vous ronflé ou toussé bruyamment ?', 55, null, 4),
('Au cours du dernier mois, avez-vous eu froid pendant la nuit ?', 40, null, 4),
('Au cours du dernier mois, avez-vous eu trop chaud pendant la nuit ?', 40, null, 4),
('Au cours du dernier mois, avez-vous fait de mauvais rêves ?', 60, null, 4),
('Au cours du dernier mois, avez-vous ressenti des douleurs qui vous ont réveillé(e) ?', 70, null, 4),
('Au cours du dernier mois, avez-vous évalué la qualité globale de votre sommeil comme mauvaise ?', 90, null, 4),
('Au cours du dernier mois, avez-vous utilisé des somnifères ou des aides au sommeil ?', 75, null, 4),
('Au cours du dernier mois, avez-vous eu du mal à rester éveillé(e) dans la journée ?', 80, null, 4),
('Au cours du dernier mois, avez-vous manqué d''entrain à cause d''un manque de sommeil ?', 85, null, 4)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(4, 0, 300, 'FAIBLE', 'Votre qualité de sommeil est bonne. Continuez à maintenir des habitudes de sommeil régulières.'),
(4, 301, 600, 'MODERE', 'Votre qualité de sommeil est altérée. Quelques ajustements de votre hygiène de sommeil pourraient significativement l''améliorer.'),
(4, 601, 1100, 'ELEVE', 'Votre qualité de sommeil est mauvaise. Il serait utile d''en parler à un médecin pour identifier les causes et trouver des solutions adaptées.')
ON CONFLICT DO NOTHING;

-- Quiz 5
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Au cours du dernier mois, vous êtes-vous senti(e) bouleversé(e) par quelque chose d''inattendu ?', 65, null, 5),
('Au cours du dernier mois, vous êtes-vous senti(e) incapable de contrôler les aspects importants de votre vie ?', 80, null, 5),
('Au cours du dernier mois, vous êtes-vous senti(e) nerveux/nerveuse ou stressé(e) ?', 75, null, 5),
('Au cours du dernier mois, avez-vous été incapable de faire face à tout ce que vous deviez faire ?', 85, null, 5),
('Au cours du dernier mois, avez-vous été irrité(e) par des choses hors de votre contrôle ?', 70, null, 5),
('Au cours du dernier mois, vous êtes-vous senti(e) dépassé(e) par les événements ?', 90, null, 5),
('Au cours du dernier mois, avez-vous eu du mal à vous concentrer à cause du stress ?', 75, null, 5),
('Au cours du dernier mois, avez-vous ressenti de la tension musculaire en raison du stress ?', 60, null, 5),
('Au cours du dernier mois, avez-vous eu des maux de tête liés au stress ?', 55, null, 5),
('Au cours du dernier mois, avez-vous mangé davantage ou moins à cause du stress ?', 50, null, 5)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(5, 0, 250, 'FAIBLE', 'Votre niveau de stress perçu est faible. Vous semblez gérer efficacement les exigences de votre quotidien.'),
(5, 251, 500, 'MODERE', 'Votre niveau de stress perçu est modéré. Quelques stratégies de gestion du stress pourraient vous aider à retrouver un meilleur équilibre.'),
(5, 501, 760, 'ELEVE', 'Votre niveau de stress perçu est élevé. Il est recommandé de consulter un professionnel et d''adopter activement des stratégies de réduction du stress.')
ON CONFLICT DO NOTHING;

-- Quiz 6
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Je me sens émotionnellement épuisé(e) par mes études.', 85, null, 6),
('Je me sens à bout à la fin d''une journée de cours ou de travail universitaire.', 75, null, 6),
('Je me sens fatigué(e) quand je me lève et que je dois affronter une nouvelle journée d''études.', 80, null, 6),
('Étudier ou assister aux cours me demande un effort intense.', 70, null, 6),
('Je me sens épuisé(e) à cause de mes études.', 90, null, 6),
('Je suis devenu(e) moins intéressé(e) par mes études depuis que j''ai commencé cette formation.', 65, null, 6),
('Je suis devenu(e) moins enthousiaste vis-à-vis de mes études.', 70, null, 6),
('Je suis devenu(e) cynique quant à l''utilité de mes études.', 75, null, 6),
('Je doute de la valeur de mes études.', 80, null, 6),
('Je gère difficilement les problèmes qui surviennent dans mes études.', 60, null, 6),
('J''ai du mal à trouver de la satisfaction dans l''accomplissement de mes tâches universitaires.', 65, null, 6),
('Je ne me sens plus stimulé(e) lorsque j''accomplis quelque chose dans mes études.', 70, null, 6)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(6, 0, 300, 'FAIBLE', 'Vous ne présentez pas de signes significatifs d''épuisement étudiant. Votre engagement et votre énergie semblent bien préservés.'),
(6, 301, 600, 'MODERE', 'Des signes modérés d''épuisement sont présents. Prendre soin de vous, vous accorder des pauses et en parler si besoin est important.'),
(6, 601, 935, 'ELEVE', 'Vous présentez des signes importants d''épuisement étudiant. Consulter un conseiller ou un médecin est fortement recommandé.')
ON CONFLICT DO NOTHING;

-- Quiz 7
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Sautez-vous régulièrement des repas dans la journée ?', 70, null, 7),
('Consommez-vous plus de 3 cafés ou boissons caféinées par jour ?', 60, null, 7),
('Mangez-vous souvent devant un écran ou en faisant autre chose ?', 50, null, 7),
('Consommez-vous régulièrement des plats ultra-transformés ou de la restauration rapide ?', 75, null, 7),
('Buvez-vous moins de 1,5 litre d''eau par jour ?', 65, null, 7),
('Consommez-vous moins de 5 portions de fruits et légumes par jour ?', 70, null, 7),
('Grignotez-vous fréquemment des aliments sucrés ou salés entre les repas ?', 60, null, 7),
('Consommez-vous de l''alcool plus de 2 fois par semaine ?', 80, null, 7),
('Avez-vous tendance à manger davantage lorsque vous êtes stressé(e) ou triste ?', 75, null, 7),
('Votre alimentation est-elle très irrégulière selon les jours ?', 65, null, 7)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(7, 0, 200, 'FAIBLE', 'Vos habitudes alimentaires semblent globalement équilibrées. Continuez à prendre soin de votre alimentation.'),
(7, 201, 450, 'MODERE', 'Quelques habitudes alimentaires pourraient être améliorées. Des ajustements progressifs peuvent avoir un impact positif sur votre énergie et votre bien-être.'),
(7, 451, 720, 'ELEVE', 'Vos habitudes alimentaires présentent plusieurs déséquilibres. Un accompagnement par un professionnel de santé ou un diététicien pourrait vous être bénéfique.')
ON CONFLICT DO NOTHING;

-- Quiz 8
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Pratiquez-vous moins de 150 minutes d''activité physique modérée par semaine ?', 85, null, 8),
('Passez-vous plus de 8 heures par jour en position assise ?', 75, null, 8),
('Utilisez-vous principalement les transports motorisés pour vous déplacer au quotidien ?', 55, null, 8),
('Évitez-vous les escaliers au profit des ascenseurs ou escalators ?', 45, null, 8),
('Avez-vous arrêté une activité sportive depuis plus de 6 mois sans la remplacer ?', 70, null, 8),
('Ressentez-vous une fatigue importante après un effort physique léger ?', 80, null, 8),
('Manquez-vous de souffle pour des efforts du quotidien (monter des escaliers, marcher vite) ?', 90, null, 8),
('Évitez-vous de bouger par manque de motivation ou d''énergie ?', 75, null, 8),
('Votre activité physique est-elle inexistante la plupart des semaines ?', 80, null, 8)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(8, 0, 200, 'FAIBLE', 'Votre niveau d''activité physique est satisfaisant. Continuez à intégrer le mouvement dans votre quotidien.'),
(8, 201, 450, 'MODERE', 'Votre niveau d''activité physique est insuffisant. Essayez d''intégrer progressivement plus de mouvement dans vos journées.'),
(8, 451, 655, 'ELEVE', 'Votre sédentarité est importante. Commencer par de petits objectifs quotidiens (marche, étirements) peut déjà faire une vraie différence pour votre santé.')
ON CONFLICT DO NOTHING;

-- Quiz 9
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Avez-vous le sentiment de manquer de relations proches et significatives ?', 90, null, 9),
('Passez-vous la majorité de vos journées sans interaction sociale réelle ?', 85, null, 9),
('Avez-vous du mal à parler de vos difficultés à quelqu''un en qui vous avez confiance ?', 80, null, 9),
('Vous sentez-vous incompris(e) ou mal à l''aise dans les interactions sociales ?', 70, null, 9),
('Avez-vous refusé des invitations sociales à cause d''anxiété ou de manque d''envie ?', 65, null, 9),
('Vous sentez-vous seul(e) même lorsque vous êtes entouré(e) de personnes ?', 85, null, 9),
('Avez-vous l''impression que personne ne s''intéresse vraiment à vous ?', 90, null, 9),
('Évitez-vous les activités collectives ou de groupe ?', 60, null, 9),
('Avez-vous du mal à nouer de nouvelles relations ?', 65, null, 9),
('Votre réseau social s''est-il réduit significativement au cours des derniers mois ?', 75, null, 9)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(9, 0, 200, 'FAIBLE', 'Votre réseau de soutien social semble bien établi. Continuez à entretenir ces liens précieux.'),
(9, 201, 500, 'MODERE', 'Votre lien social est présent mais pourrait être renforcé. Chercher à élargir votre réseau ou à approfondir des relations existantes peut améliorer votre bien-être.'),
(9, 501, 765, 'ELEVE', 'Des signes d''isolement social important sont présents. En parler à un professionnel ou rejoindre des groupes ou associations peut vous aider à recréer du lien.')
ON CONFLICT DO NOTHING;

-- Quiz 10
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Remettez-vous souvent au lendemain des tâches que vous pourriez faire aujourd''hui ?', 80, null, 10),
('Attendez-vous souvent la dernière minute pour commencer un travail important ?', 85, null, 10),
('Évitez-vous de commencer des tâches difficiles ou peu agréables ?', 75, null, 10),
('Vous laissez-vous facilement distraire quand vous devriez travailler ?', 70, null, 10),
('Avez-vous du mal à démarrer une tâche même quand vous savez qu''elle est urgente ?', 85, null, 10),
('Passez-vous du temps à planifier sans jamais vraiment commencer ?', 65, null, 10),
('Attendez-vous d''être dans les conditions parfaites avant de commencer ?', 60, null, 10),
('Ressentez-vous de la culpabilité ou de l''anxiété à cause de vos reports ?', 70, null, 10),
('Vos reports ont-ils déjà eu des conséquences négatives sur vos résultats ?', 90, null, 10),
('Avez-vous l''impression de ne jamais avoir le temps même si vous en avez objectivement ?', 75, null, 10)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(10, 0, 200, 'FAIBLE', 'Votre tendance à procrastiner est faible. Vous semblez gérer efficacement vos priorités.'),
(10, 201, 500, 'MODERE', 'Vous avez une tendance modérée à procrastiner. Des techniques comme la méthode Pomodoro ou la règle des 2 minutes peuvent vous aider.'),
(10, 501, 755, 'ELEVE', 'Votre procrastination est importante et impacte probablement votre bien-être et vos résultats. Un accompagnement pour en comprendre les causes profondes peut être utile.')
ON CONFLICT DO NOTHING;

-- Quiz 11
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Passez-vous plus de 4 heures par jour sur les réseaux sociaux ou à naviguer sans but précis ?', 75, null, 11),
('Vérifiez-vous votre téléphone dès votre réveil avant de faire quoi que ce soit d''autre ?', 70, null, 11),
('Avez-vous du mal à vous concentrer sans vérifier vos notifications régulièrement ?', 80, null, 11),
('Vous sentez-vous anxieux/anxieuse lorsque vous n''avez pas accès à votre téléphone ?', 85, null, 11),
('Utilisez-vous les écrans pour éviter de penser à vos problèmes ?', 75, null, 11),
('Le temps passé sur les écrans empiète-t-il sur votre sommeil ?', 80, null, 11),
('Avez-vous déjà tenté de réduire votre temps d''écran sans y parvenir ?', 70, null, 11),
('Vous comparez-vous souvent défavorablement aux personnes que vous voyez sur les réseaux ?', 65, null, 11),
('Le numérique interfère-t-il avec vos relations en face à face ?', 75, null, 11),
('Ressentez-vous le besoin d''être constamment disponible et joignable ?', 60, null, 11)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(11, 0, 200, 'FAIBLE', 'Votre rapport au numérique semble équilibré. Vous utilisez les écrans sans en être dépendant(e).'),
(11, 201, 500, 'MODERE', 'Votre usage du numérique présente quelques signes préoccupants. Établir des règles claires sur votre temps d''écran peut améliorer votre qualité de vie.'),
(11, 501, 735, 'ELEVE', 'Votre usage du numérique semble problématique. Une démarche de réduction progressive, voire un soutien professionnel, pourrait vous aider à retrouver un meilleur équilibre.')
ON CONFLICT DO NOTHING;

-- Quiz 12
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Avez-vous le sentiment de ne pas valoir grand-chose ?', 90, null, 12),
('Pensez-vous que vous avez peu de qualités dont vous pouvez être fier(e) ?', 85, null, 12),
('Avez-vous tendance à penser que vous êtes un(e) raté(e) ?', 100, null, 12),
('Avez-vous l''impression de ne pas mériter le respect des autres autant qu''ils le méritent ?', 80, null, 12),
('Adoptez-vous une attitude négative envers vous-même ?', 85, null, 12),
('Manquez-vous souvent de confiance en vous pour entreprendre de nouvelles choses ?', 75, null, 12),
('Avez-vous tendance à vous critiquer sévèrement après une erreur ?', 70, null, 12),
('Pensez-vous que vos opinions comptent moins que celles des autres ?', 65, null, 12),
('Avez-vous du mal à accepter des compliments ou de la reconnaissance ?', 60, null, 12),
('Avez-vous souvent peur d''être jugé(e) négativement par les autres ?', 70, null, 12)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(12, 0, 200, 'FAIBLE', 'Votre estime de vous-même semble satisfaisante. Vous avez globalement une image positive de vous.'),
(12, 201, 500, 'MODERE', 'Votre estime de vous-même est fragile par moments. Travailler sur la bienveillance envers soi-même peut vous aider à renforcer votre confiance.'),
(12, 501, 780, 'ELEVE', 'Votre estime de vous-même est basse. Un accompagnement psychologique peut vous aider à développer une image plus juste et plus positive de vous-même.')
ON CONFLICT DO NOTHING;

-- Quiz 13
INSERT INTO question (statement, score_value, correct_answer, quiz_id) VALUES
('Avez-vous du mal à identifier ce que vous ressentez exactement ?', 75, null, 13),
('Vos émotions vous débordent-elles souvent et vous empêchent-elles d''agir efficacement ?', 85, null, 13),
('Avez-vous tendance à ruminer longtemps après un événement stressant ?', 80, null, 13),
('Avez-vous du mal à vous calmer une fois que vous êtes en colère ou anxieux/anxieuse ?', 80, null, 13),
('Évitez-vous certaines situations pour ne pas ressentir des émotions difficiles ?', 70, null, 13),
('Avez-vous l''impression que vos émotions contrôlent vos décisions plus que votre raison ?', 75, null, 13),
('Avez-vous du mal à exprimer vos émotions à vos proches ?', 65, null, 13),
('Êtes-vous facilement affecté(e) par l''humeur des personnes autour de vous ?', 55, null, 13),
('Ressentez-vous souvent des émotions intenses sans savoir d''où elles viennent ?', 70, null, 13),
('Avez-vous du mal à distinguer vos émotions des faits réels d''une situation ?', 75, null, 13),
('Utilisez-vous des comportements négatifs (alcool, isolement, suralimentation) pour gérer vos émotions ?', 95, null, 13)
ON CONFLICT (statement, quiz_id) DO NOTHING;

INSERT INTO result_message_config (quiz_id, min_score, max_score, risk_level, message) VALUES
(13, 0, 200, 'FAIBLE', 'Vous semblez avoir une bonne capacité à identifier et réguler vos émotions. Continuez à développer votre intelligence émotionnelle.'),
(13, 201, 500, 'MODERE', 'Votre gestion des émotions présente quelques difficultés. Des pratiques comme la pleine conscience ou la tenue d''un journal émotionnel peuvent vous aider.'),
(13, 501, 825, 'ELEVE', 'Vous éprouvez des difficultés importantes à gérer vos émotions. Un accompagnement par un psychologue ou un thérapeute peut vous apporter des outils concrets et efficaces.')
ON CONFLICT DO NOTHING;