package org.mtransit.parser.ca_quebec_orleans_express_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CleanUtils;
import org.mtransit.commons.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.mt.data.MAgency;

import java.util.Locale;
import java.util.regex.Pattern;

import static org.mtransit.commons.StringUtils.EMPTY;

// https://gtfs.keolis.ca/gtfs.zip
public class QuebecOrleansExpressBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new QuebecOrleansExpressBusAgencyTools().start(args);
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Orleans Express";
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@NotNull
	@Override
	public String cleanRouteLongName(@NotNull String routeLongName) {
		routeLongName = CleanUtils.SAINT.matcher(routeLongName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESIS1.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESIS1_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESIS2.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESIS2_REPLACEMENT);
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String RID_1 = "1";
	private static final String RID_2 = "2";
	private static final String RID_3 = "3";
	private static final String RID_4 = "4";
	private static final String RID_5 = "5";
	private static final String RID_6 = "6";
	private static final String RID_7 = "7";

	@Nullable
	@Override
	public String getRouteShortName(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteShortName())) {
			//noinspection deprecation
			final String routeId = gRoute.getRouteId();
			switch (routeId) {
			case RID_1:
				return "MT QC S"; // Montréal - Québec ( Express )
			case RID_2:
				return "MT YUL"; // Montréal - Aéroport Montréal-Trudeau
			case RID_3:
				return "QC RK"; // Bas-Saint-Laurent
			case RID_4:
				return "RK GS S"; // Gaspésie ( Côté Sud De La Péninsule )
			case RID_5:
				return "RK GS N"; // Gaspésie ( Côté Nord De La Péninsule )
			case RID_6:
				return "MT QC N"; // Montréal - Québec ( Mauricie )
			case RID_7:
				return "MT VT"; // Centre-du-Québec
			}
		}
		return super.getRouteShortName(gRoute);
	}

	private static final String AGENCY_COLOR = "01ADB9";

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor())) {
			//noinspection deprecation
			final String routeId = gRoute.getRouteId();
			if (RID_1.equals(routeId)) return "4E76BA";
			if (RID_2.equals(routeId)) return "4E76BA";
			if (RID_3.equals(routeId)) return "BF2026";
			if (RID_4.equals(routeId)) return "69BD45";
			if (RID_5.equals(routeId)) return "69BD45";
			if (RID_6.equals(routeId)) return "F89843";
			if (RID_7.equals(routeId)) return "01ADB9";
		}
		return super.getRouteColor(gRoute);
	}

	@Override
	public boolean directionSplitterEnabled() {
		return true;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	private static final String CENTRE_VILLE = "Centre-Ville";
	private static final Pattern FIX_CENTRE_VILLE_ = Pattern.compile("((^|\\W)(centre ville)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String FIX_CENTRE_VILLE_REPLACEMENT = "$2" + CENTRE_VILLE + "$4";

	private static final Pattern REMOVE_ENDS_W_CENTRE_VILLE_ = Pattern.compile("(( \\(centre-ville\\))(\\W|$))");

	private static final String UNIVERSITE_LAVAL = "Université Laval";
	private static final Pattern QUEBEC_UNIVERSITE_LAVAL_ = Pattern.compile("((^|\\W)(qu[é|e]bec \\(université laval\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);
	private static final String QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT = "$2" + UNIVERSITE_LAVAL + "$4";

	private static final String AEROPORT_TRUDEAU = "Aéroport Trudeau";
	private static final Pattern MONTREAL_AEROPORT_TRUDEAU_ = Pattern.compile("((^|\\W)(montr[é|e]al \\(a[é|e]roport trudeau\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final String MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT = "$2" + AEROPORT_TRUDEAU + "$4";

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = FIX_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(FIX_CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = REMOVE_ENDS_W_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = QUEBEC_UNIVERSITE_LAVAL_.matcher(tripHeadsign).replaceAll(QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT);
		tripHeadsign = MONTREAL_AEROPORT_TRUDEAU_.matcher(tripHeadsign).replaceAll(MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanBounds(Locale.FRENCH, tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypesFRCA(tripHeadsign);
		return CleanUtils.cleanLabelFR(tripHeadsign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.cleanBounds(Locale.FRENCH, gStopName);
		gStopName = CleanUtils.cleanStreetTypesFRCA(gStopName);
		return CleanUtils.cleanLabelFR(gStopName);
	}
}
