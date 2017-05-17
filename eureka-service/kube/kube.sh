kube_deployment_name=$1
kubectl delete -f ${kube_deployment_name}
kubectl create -f ${kube_deployment_name}
kubectl get all -n spring-cloud
