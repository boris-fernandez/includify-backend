import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from sklearn.cluster import KMeans
import random

# nombres=["hola", "perro", "nuevo", "nunca", "enemigo", "bendicion", 
#          "carros", "gato", "heroe", "lider", "futbolista", "control",
#          "absoluto", "salvacion", "fe", "comer", "caminar", "elefante"]

# clientes = pd.DataFrame({"saldo" : [50000, 45000, 48000, 43500, 47000, 52000, 
#                                     20000, 26000, 25000, 23000, 21400, 18000,
#                                     8000, 12000, 6000, 14500, 12600, 7000],
                         
#                          "transacciones": [25, 20, 16, 23, 25, 18,
#                                            23, 22, 24, 21, 27, 18,
#                                            8, 3, 6, 4, 9, 3],
                                           
#                          "dinero" : [530000, 425000, 48000, 435400, 470200, 529000, 
#                                     200, 2000, 25000, 2300, 21400, 184000,
#                                     80900, 1200, 6000, 14500, 192600, 70900]})

# columns=["saldo", "transacciones", "dinero"]

def procesarClustering(nombres, clientes, columns):
    escalador = MinMaxScaler().fit(clientes.values)

    clientes = pd.DataFrame(escalador.transform(clientes.values),
                        columns=columns)

    kmeans = KMeans(n_clusters=7).fit(clientes.values)

    clientes["cluster"] = kmeans.labels_
    clusterReferencia = kmeans.labels_[0]
    # print(f"El primer punto pertenece al cluster: {clusterReferencia}")
    clustersResultados = kmeans.labels_.tolist()
    del clustersResultados[0]

    # print(clustersResultados)

    listaTemporal = []
    listaRecomendaciones = []
    n=1
    for valores in clustersResultados:
        if clusterReferencia == valores:
            listaTemporal.append(nombres[n])
        n=n+1
    random.shuffle(listaTemporal)
    # print(f"Cluster Referencia {clusterReferencia}: {listaTemporal}")
    # print("fin cluster")

    for elemento in listaTemporal:
        listaRecomendaciones.append(elemento)

    for ncluster in range(7):
        listaTemporal = []
        n=1
        for valores in clustersResultados:
            if ncluster == valores and ncluster != clusterReferencia:
                listaTemporal.append(nombres[n])
            n=n+1
        # print(f"Cluster {ncluster}: {listaTemporal}")
        random.shuffle(listaTemporal)
        for elemento in listaTemporal:
            listaRecomendaciones.append(int(elemento))
        # print("fin cluster")

    return(listaRecomendaciones)

# print(procesarClustering(nombres, clientes, columns))