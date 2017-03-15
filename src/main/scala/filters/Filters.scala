package filters

import javax.inject.Inject

import com.kenshoo.play.metrics.MetricsFilter
import play.api.http.DefaultHttpFilters
import play.filters.gzip.GzipFilter

class Filters @Inject() (gzipFilter: GzipFilter, logFilter: LoggingFilter, metricsFilter: MetricsFilter)
  extends DefaultHttpFilters(gzipFilter, logFilter, metricsFilter)
