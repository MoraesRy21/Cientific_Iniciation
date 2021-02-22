# Sistema de Segurança com Reconhecimento Facial
Um projeto iniciado na Faculdade de Ciências e Tecnologia ÁREA1 para o Programa de Iniciação Científica e Tecnológica (PICT).
***
#### Artigo
Para mais informações sobre a arquitetura e conceitos usados no projeto, vale a leitura do artigo publicado em: 
***
## Estrutura de Projeto
O projeto está dividido em três aplicações e um banco de dados:
- **Sistema_de_Seguranca**, desenvolvido no NetBeans IDE 8.2
- **Sistema_de_Validacao**, desenvolvido no NetBeans IDE 8.2
- **ProjectServer_IA**, desenvolvido no IntelliJ IDEA Community
- Banco de Dados MySQL 8.x
 

## Libs e Drives
- **OpenCV** 4.2 para Java 
- **JavaFX** para Java 8
- Drive de conexão com banco de dados MySQL 8.x **(mysql-connector-java-5.1.45)**

***

### Observações
Para ser feito comunicação entre as aplicações se faz necessário a alteração no arquivo **aplication.properties** de cada aplicação. 
Caso queira que a comunicação seja criptografada entre as aplicações será necessário a utilização de uma VPN, como sugestão o  **OpenVPN** pois foi usado e testado, e fazer novamente a adequação no **aplication.properties**