INSERT INTO public.contrato(
            id, created, updated, biometria, nome_paciente, numero, 
            valor_total, usuario_id,ativo)
    VALUES (1, current_timestamp, null, '\x53554e53557a49784141414a6d414d4241414141414d55417851414541537742414141416733496477774159414a775051674134415067506b774134414a41507151424a4142385079774256414a385072674261414a73505467427341476f504d774238414f73503441435541435950565143564147554f52514368414f554f4677436d4146495045414443414e4d5053514450414f495071774453414b59503167446a41436f50625144744143384f597744794145414e647744354143384e5267443841456f4d50514439414e554d7451442b414430504877442f414d304e7751414241536f5031774149416167505967414b4154304d527741564162304d6c6741614161515067414163416130504f3335504479502f773548544434747244584d57393976336c5958396b334b4258516479675a38486d5833682f346f50676f425342383931676f444743724b4c4434762f6a36502f675948572f375a2f6759464b2f74734565594d726241747065594f373835767a787166696d312f38456d767266417439736747576862377677504e31414b4c304c424e4e45594547395144683862583373485a316a696f4d7149376447796f49544264536c6161627376697968737351344f396c686a49425a594a6d625a7142426634704537574467585a31363757445766724361333533305170716359714233722f51427941794151494d47566344414b5541462f3448414c3441477637412f734c2b4351437a4152772b7755554b414a7343484d444177663741525177416751514a2f763342524d442f7766384d4149454e4544372f5663444177517341564245414d455a4d444142424976482b4d66382f536763415179763077507a2f4d7751414f79397764776341517a54774b6b494441443435634d494a414559352b6b442f2f6a4d4a414b314a4846685677516f41716b306b776342625a4245414a6b2f7777432f2b2f7341792f2f2f41577755417a6c67657745735841436465352f38784e662f2b2f2f2f422f734645774d466146414253612f66392f7937417750316b57467a41426741766632664477473461414136473169762f2f452f2b7750372b54465841577344414477425a6c5054382f66372f2f73442f2f2f2f432f384c2f4241446b6c534130426744656d4364722f42734143707258774376412f44762f2f2f372f2f73482b774d44412f38484177662f41464141616f747a2f5076372b2f544e412f316241426742427057423277514d414536686177685941464d4858547a332f2f50332b2f7a74547745594e41456a4c317633392b2f7a2b5076386d4367424e7a747a392b2f762b2f2f3434447743707a366c2b776354436f3337414267424830315a6e2f514d413275517277526f41427537472f4648415276332b2f66372b2f762f2f2f2f2f412f38444277763045414e6a764d494d46414772384f706f4541484479505a674d41447a35304544392b2f722b2f2f2f2b4242437243696c66457741692b39664177476a2f49507a2b774d44412f634839436742422f4e4d2b2b6676384d674d516f68416e77415941572f7a4a2b7658362f675541642f343077356b444544734256384145454d55434b563045454c59444e356745454c38464d49734f45457352796350362f7637332b2f332f7776396b41424141677a346371674150414a6f5064774175414934506b514241414238507367424c414a30506c674251414a6b504e41426d41476b4f48514230414f6b4f7877434a41434d504f774351414751504d41435a414f4d506b774449414b59504c77444b414e73504441444c41456b505167446d414f634f4c77446e41454d505567447141444d4f5a5144724144594f544144744144304f367744334144305044774143416230504851414341635550785141434161635074414161415a774f6c774162415a734f54674164416341505a674165415551507351416741596b4f6d414169415a674e4f3335544479502f446e4158394e2f336c596a366b484b4658516479675a38486e587a682f34734d67594e5342312b4a67594f326873594c453473446a37742f6759432b6674622f6759424b2f39384951334e4c642f3844355a764f395361564e6e46574333654f744b2b6c74426d6c50563339613059497650725a544648397a51584a2b744a524d4137635730454b757071766f3675337866697166344e3250516957684b50674e513879496335754f425a6c41316b4849416274462b5544775836446849654e7659593771454f747a4f3531386f487a7050376c4574332f37744d39446941744151486b47446b48414b4541476a5a57427743764142354d57514d41775151652f676b416951455754457a424467426141677a412f7639557746467043674237416864555648414c41454d4c2f6637397750394753514d414a682f702f516f41534251442f7a466b2f7759414d5348304b7638474143737339502f3951516f416c45416156566a2b775177414330506e2f2f314c4d7a774b414a4a454a4d484157384661427743315468355951684541435648694b544c2b2f2f372b776a62414741415456656e2f2f6a50422f532f2f77502f41537342642f5251414f4754332f76372f4b6341335563426c2f526f4142572f674d502f2b2f7a4d76524d44412f734c41776634334177415a65474c42475141446774722f2f6a442b2f796b31774d42462f3846674277444c69534441532f345041442b4e38667a394d634176774d4a56427744476a5366415866344641433263586e735941414f68326a332b7776763977507a2f7750372f2f3844422f734841774d444147514144744e5a474d4351342f6b54422f38426b7742634141385463632f2f2b2f7637392f66372f4e557242574241416b73576e654d484577385443777348432f384c44445141777839662f2f667a362f7637412f2f2f2f7751515173416b6b555173414d4f4c542f762f384744594941454c6a327633322b2f374177415141522b56477763414541462f385053774641464c754d6630734177424d386a722f44414134394d7a422f5033342f507a412f6c7744454a514e4a386351414a6a32716342786a385446774d48426d784d41484f664a2f634c2b2f2f37392b2f33392f2f374277503741774d4545454777464f6f5944454d6b474c6349444550344a51384144454877514a384941494143437567393041426b416b41394f4142734168773675414467416e413874414667412b51392b414667416b412b33414851416e512b58414830416d7738374149774161673865414a304136772f4a414b384149673942414c30416241347a414d4941367736584150454170673874415049413267394e414177424377397943697344422f66362b6b2b54482f745044794d4465772b2f6b6c393330784d6139397633422f6461424a384447332f61414934504233754367564d417a334b43676349447434415868774f4c75332b4a674c65413077534a67454944337842506e376548637a2f43306d2f77643349534d69736a613266374b7941754151482b47637749414745424530624254674d416f6745612f776b416c4149582f73484150734144414651464538454c4149734946762f2f774d424d774d414541454d4d44456f4d41496b4e494d482f5538444177466b4c41486762467639694e6c6b4f414649634355762f2f38444156566b4541456f65674777544143596d43634e424f4d42442f317054445141744f50542f4b6b64452f7730414a556a702f66387177454d2b427741765650416f4e6773416646535777734b477773463641774170576e444343514178577670424f2f384d41494a62463845325538453143674356625344415a465843434143376478355977446f4e414a4e386e48654a686f5146414a7541494d444177424941503476332f662f2f2f546a412f6c317277526f41415a44614d54582b4f7a354b5238484177662f2f475141446e4e77764b5034374e6b52546138454541427168597067594141656b346b2f3977502f2b2f762f3977502f2f774d442f776637432f384c2f4241444e7279426b4277444a73796642774d42534541417a76656e392f79482f2f38442b2f384842626759414d4d56657773444178526b4141396a50775037392f3844412f66332b2f63442b2f38442b2f3848412f3848412f3849564142486a30432f2b2f2f7a394b6634332f734841575241414c6533542f2f33392f7676382f30722b77473850414a62747033695078634843777342384451417838746f302b76332b51565944414376315673455a414172377573443577662f2b2f2f2f2b2f76332b2f63442f2f762f2f776637432f38482b4178414b4254334242524252436633334e7734514a424865626637372f50732b506751515368503946674d5154526b542f6773514c52725477502f2f2f5076374b676f514d522f58595072352f536f4945454d53312f37352b503156414552434151454141414157414141414141494641414141414141415255493d
', 'Henrique Nitatori', '1', 
            1000.00, null,true);

