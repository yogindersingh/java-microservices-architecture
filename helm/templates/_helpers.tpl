{{/*
Expand the name of the chart.
*/}}
{{- define "java-microservices.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Chart name and version label.
*/}}
{{- define "java-microservices.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels applied to every resource.
*/}}
{{- define "java-microservices.labels" -}}
helm.sh/chart: {{ include "java-microservices.chart" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: {{ include "java-microservices.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Env vars from microservice-base-config (rabbitmq, OTEL agent).
*/}}
{{- define "java-microservices.microserviceBaseEnv" -}}
- name: SPRING_RABBITMQ_HOST
  value: {{ .Values.commonEnv.springRabbitmqHost | quote }}
- name: JAVA_TOOL_OPTIONS
  value: {{ .Values.commonEnv.javaToolOptions | quote }}
- name: OTEL_EXPORTER_OTLP_ENDPOINT
  value: {{ .Values.commonEnv.otelExporterOtlpEndpoint | quote }}
- name: OTEL_METRICS_EXPORTER
  value: {{ .Values.commonEnv.otelMetricsExporter | quote }}
- name: OTEL_LOGS_EXPORTER
  value: {{ .Values.commonEnv.otelLogsExporter | quote }}
{{- end }}

{{/*
Env vars from microservice-configserver-config (extends base + config server + eureka).
*/}}
{{- define "java-microservices.configServerEnv" -}}
{{ include "java-microservices.microserviceBaseEnv" . }}
- name: SPRING_PROFILES_ACTIVE
  value: {{ .Values.commonEnv.springProfilesActive | quote }}
- name: SPRING_CONFIG_IMPORT
  value: {{ .Values.commonEnv.configServerUrl | quote }}
- name: EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE
  value: {{ .Values.commonEnv.eurekaDefaultZone | quote }}
{{- end }}
