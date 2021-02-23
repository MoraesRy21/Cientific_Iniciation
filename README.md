

# Sistema de Segurança com Reconhecimento Facial
Um projeto iniciado na Faculdade de Ciências e Tecnologia ÁREA1 para o Programa de Iniciação Científica e Tecnológica (PICT).

## Objetivo 
Desenvolver um software que forneça segurança a um determinado local, com baixo custo de implementação com alta eficiência.   

## Artigo
Para mais informações sobre a arquitetura e conceitos usados no projeto, vale a leitura do artigo publicado em: 

## Estrutura de Projeto
O projeto está dividido em três aplicações e um banco de dados:
- **Sistema_de_Seguranca**, desenvolvido no NetBeans IDE 8.2
- **Sistema_de_Validacao**, desenvolvido no NetBeans IDE 8.2
- **ProjectServer_IA**, desenvolvido no IntelliJ IDEA Community
- Banco de Dados MySQL 8.x
 

## Pré requisitos
 - **Sistema_de_Seguranca:**
    - Java versão 8
    - OpenCV 4.2 para Java
    - JavaFX para Java 8
    - Driver de conexão  com banco de dados MySQL 8.x **(mysql-connector-java-5.1.45)**
  - **Sistema_de_Validacao:**
    - Java versão 8
    - OpenCV 4.2 para Java
    - JavaFX para Java 8
    - Driver de conexão  com banco de dados MySQL 8.x **(mysql-connector-java-5.1.45)**
  - **ProjectServer_IA:**
    - Java versão 8
    - [Download da rede neural](https://dl4jdata.blob.core.windows.net/models/vgg16_dl4j_vggface_inference.v1.zip)
    - Copiar o arquivo baixado (vgg16_dl4j_vggface_inference.v1.zip) para a pasta "C:/Users/$user/.deeplearning4j"

## Execução 
- **Sistema_de_Seguranca:**

    Para executar basta importar as bibliotecas e configurar o `Djava.library.path` com o caminho das DLLs do OpenCV.

- **Sistema_de_Validacao:**

    Da mesma forma que o anterior, basta importar as bibliotecas e configurar o `Djava.library.path` com o caminho das DLLs do OpenCV.

- **ProjectServer_IA:**
  
   Antes de executar é necessário o download das depenecias do projeto, para isso basta executar o script do Gradle e consequentemente a atualização através do repositório Maven.


***
### Observações
Para ser feito comunicação entre as aplicações se faz necessário a alteração no arquivo **aplication.properties** de cada aplicação. Neste arquivo é feito as configurações IPs.  
Caso queira que a comunicação seja criptografada entre as aplicações será necessário a utilização de uma VPN, como sugestão o **OpenVPN** pois além de ser *Opensource*, foi usado e testado. Porém será necessário alterar o arquivo **aplication.properties**, para colocar o IP privado da VPN. 

