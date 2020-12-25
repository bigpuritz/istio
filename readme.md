##About 

An example project demonstrating 3 Spring-Boot applications communicating with each other 
and deployed on Kubernetes (Minikube) / ISTIO Service Mesh.

Tools:

* [Istio](https://istio.io/)
* [Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)
* [Gradle](https://gradle.org/)
* [Gradle JIB-Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
* [Skaffold](https://skaffold.dev/)
* [Dive](https://github.com/wagoodman/dive)



### Dashboards

* `minikube dashboard`
* `istioctl dashboard kiali`

### Label Namespace to be ISTIO enabled

* `kubectl label namespace default istio-injection=enabled`

### Deployments

* `kubectl apply -f istio-istio-service1/k8s.yaml`
* `kubectl apply -f istio-istio-service2/k8s.yaml`
* `kubectl apply -f istio-istio-service3/k8s.yaml`
* `kubectl apply -f istio-service1-gtwy-vs.yaml`

### Ingress IP and ports / Tunnel

* `export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')`
* `export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')`
* `export INGRESS_HOST=$(minikube ip)`
* `export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT`
* `minikube tunnel`

### URLs

* `http://$GATEWAY_URL/srv1/demo/index.html`
* `http://$GATEWAY_URL/srv1/callme/ping`
* `http://$GATEWAY_URL/srv2/callme/ping`
* `http://$GATEWAY_URL/srv3/callme/ping`

### Infos

_Each command can be called with additional parameters `-o yaml` oder `-o json`_

* `istioctl analyze`
* `kubectl get services`
* `kubectl get pods`
* `kubectl get gateway`
* `kubectl get virtualservices`
* `kubectl get destinationrules`
* `kubectl delete -n default deployment istio-service1-v1`
* `kubectl delete -n default service istio-service1`
* A tool for exploring a docker image: https://github.com/wagoodman/dive


### Lokale Entwicklung

Call `skaffold` from the project root-directory:
* `skaffold dev --port-forward --filename=skaffold.yaml` 
* `skaffold dev --port-forward --filename=istio-service1/skaffold.yaml`
* `skaffold dev --port-forward --filename=istio-service2/skaffold.yaml`
* `skaffold dev --port-forward --filename=istio-service3/skaffold.yaml`

Remote-Debugging with `skaffold debug --port-forward --filename=...`
